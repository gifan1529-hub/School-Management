package com.example.schoolmanagement.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Data.Remote.ApiService
import com.example.schoolmanagement.Domain.Model.UserDetails
import com.example.schoolmanagement.Domain.UseCase.SubmitAttendanceUC
import com.example.schoolmanagement.Domain.UseCase.getDetailUserUC
import com.example.schoolmanagement.Domain.UseCase.LogoutUseCase
import com.example.schoolmanagement.getTodayDate
import com.example.schoolmanagement.getTodayDateS
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.toLocalDateTime
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone.Companion.currentSystemDefault

class HomeViewModel (
    private val prefsManager: PrefsManager,
    private val logoutUC: LogoutUseCase,
    private val getDetailUserUC: getDetailUserUC,
    private val submitAttendanceUC: SubmitAttendanceUC,
    private val apiService: ApiService
): ViewModel() {
    private val _userDetails = MutableStateFlow<UserDetails?>(null)
    val userDetails: StateFlow<UserDetails?> = _userDetails

    private val _isAlreadyAbsen = MutableStateFlow(false)
    val isAlreadyAbsen: StateFlow<Boolean> = _isAlreadyAbsen

    private val _isLoadingAbsen = MutableStateFlow(false)
    val isLoadingAbsen: StateFlow<Boolean> = _isLoadingAbsen

    private val _logoutEvent = MutableStateFlow(false)
    val logoutEvent: StateFlow<Boolean> = _logoutEvent

    private val _countHadir = MutableStateFlow("0")
    val countHadir: StateFlow<String> = _countHadir

    private val _countTelat = MutableStateFlow("0")
    val countTelat: StateFlow<String> = _countTelat

    private val _countAbsen = MutableStateFlow("0")
    val countAbsen: StateFlow<String> = _countAbsen

    private val _todayStatus = MutableStateFlow("")
    val todayStatus: StateFlow<String> = _todayStatus

    init {
        loadUserDetail()
        syncAttendanceStatus()
    }

    fun loadUserDetail() {
        viewModelScope.launch {
            val details = getDetailUserUC.invoke()
            _userDetails.value = details
        }
    }

    val userName: StateFlow<String> = prefsManager.getUserName
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(500),
            initialValue = "User"
        )

    val userRole: StateFlow<String> = prefsManager.getUserRole
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "Role"
        )

    val userNIS: StateFlow<String> = prefsManager.getNis
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "NISN"
        )

    val userPhone: StateFlow<String> = prefsManager.getUserPhone
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "234"
        )

    val userClass: StateFlow<String> = prefsManager.getClass
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "rpl"
        )

    fun logout() {
        viewModelScope.launch {
            logoutUC.invoke()
            _logoutEvent.value = true
        }
    }

    private fun syncAttendanceStatus() {
        viewModelScope.launch {
            _isLoadingAbsen.value = true
            try {
                val token = prefsManager.getAuthToken.first() ?: ""
                if (token.isNotEmpty()) {
                    // ngambil history absen dari api
                    val response = apiService.getAttendanceHistory(token)
                    val history = response.data
                    val todayDate = getTodayDateS()

                    val hadir = history.count { it.status == "Present" }
                    val telat = history.count { it.status == "Late" }
                    val absen = history.count { it.status == "Absent" }
                    val todayRecord = history.find { it.date == todayDate }
                    val hasRecordToday = todayRecord != null

                    _countHadir.value = hadir.toString()
                    _countTelat.value = telat.toString()
                    _countAbsen.value = absen.toString()
                    _todayStatus.value = todayRecord?.status ?: ""
                    _isAlreadyAbsen.value = hasRecordToday

                    // cek apakah ada absen hari ini
                    val hasAttendedToday = response.data.any { it.date == todayDate }

                    // update ke data store
                    _isAlreadyAbsen.value = hasAttendedToday
                    prefsManager.saveAbsenStatus(hasAttendedToday, todayDate)

                    println("DEBUG: Sync Success. Sudah absen: $hasAttendedToday")
                }
            } catch (e: Exception) {
                println("DEBUG: Sync Gagal: ${e.message}")
                // kalo api off, ambil dari data sotre
                _isAlreadyAbsen.value = prefsManager.getAbsenStatus.first()
            } finally {
                _isLoadingAbsen.value = false
            }
        }
    }

    fun submitAbsen(qrCode : String, lat: Double, long: Double) {
        viewModelScope.launch {
            _isLoadingAbsen.value = true

            try {
                val token = prefsManager.getAuthToken.first() ?: ""
                val result = submitAttendanceUC.invoke(qrCode, token, lat, long)

                result.onSuccess {
                    val today = getTodayDate()
                    prefsManager.saveAbsenStatus(true, today)
                    _isAlreadyAbsen.value = true
                    println("DEBUG: Absen Berhasil Disimpan ke Prefs")
                }.onFailure { e ->
                    if (e.message?.contains("Kamu sudah absen hari ini", ignoreCase = true) == true) {
                        val today = getTodayDate()
                        prefsManager.saveAbsenStatus(true, today)
                        _isAlreadyAbsen.value = true
                        println("DEBUG: Sinkronisasi Status: Server bilang sudah absen.")
                    }
                    println("DEBUG: Gagal Menyimpan Absen: ${e.message}")
                }
            } catch (e: Exception) {
                println("DEBUG: Crash woii: ${e.message}")
            } finally {
                _isLoadingAbsen.value = false
            }
        }
    }
}