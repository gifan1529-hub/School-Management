package com.example.schoolmanagement.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Domain.Model.UserDetails
import com.example.schoolmanagement.Domain.UseCase.GetAttendanceStatusUseCase
import com.example.schoolmanagement.Domain.UseCase.LogoutUseCase
import com.example.schoolmanagement.Domain.UseCase.SubmitAttendanceUC
import com.example.schoolmanagement.Domain.UseCase.getDetailUserUC
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeTeacherViewModel (
    private val prefsManager: PrefsManager,
    private val logoutUC: LogoutUseCase,
    private val getAttendanceStatusUseCase: GetAttendanceStatusUseCase,
    private val getDetailUserUC: getDetailUserUC,
    private val submitAttendanceUC: SubmitAttendanceUC,
): ViewModel() {
    private val _countHadir = MutableStateFlow("0")
    val countHadir: StateFlow<String> = _countHadir

    private val _countTelat = MutableStateFlow("0")
    val countTelat: StateFlow<String> = _countTelat

    private val _countAbsen = MutableStateFlow("0")
    val countAbsen: StateFlow<String> = _countAbsen

    private val _todayStatus = MutableStateFlow("")
    val todayStatus: StateFlow<String> = _todayStatus

    private val _userDetails = MutableStateFlow<UserDetails?>(null)
    val userDetails: StateFlow<UserDetails?> = _userDetails

    private val _isAlreadyAbsen = MutableStateFlow(false)
    val isAlreadyAbsen: StateFlow<Boolean> = _isAlreadyAbsen

    private val _isLoadingAbsen = MutableStateFlow(false)
    val isLoadingAbsen: StateFlow<Boolean> = _isLoadingAbsen

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    init {
        loadUserDetail()
        syncAttendanceStatus()
    }
    fun refreshData() {
        viewModelScope.launch {
            _isRefreshing.value = true
            // Jalankan ulang fungsi sinkronisasi data
            loadUserDetail()
            syncAttendanceStatus()
            // Beri sedikit delay agar animasi tidak terlalu cepat hilang
            delay(1000)
            _isRefreshing.value = false
        }
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

    val userClass: StateFlow<String> = prefsManager.getClass
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "rpl"
        )

    private fun syncAttendanceStatus() {
        viewModelScope.launch {
            _isLoadingAbsen.value = true
            try {
                getAttendanceStatusUseCase.invoke()
                    .onSuccess { stats ->

                        _countHadir.value = stats.hadir
                        _countTelat.value = stats.telat
                        _countAbsen.value = stats.absen
                        _todayStatus.value = stats.todayStatus
                        _isAlreadyAbsen.value = stats.isAlreadyAbsen

                        prefsManager.saveAbsenStatus(stats.isAlreadyAbsen, stats.todayDate)

                        println("DEBUG: Sync Success. Sudah absen: ${stats.isAlreadyAbsen}")
                    }.onFailure { e ->
                        println("DEBUG: Sync Gagal: ${e.message}")
                        // Kalo api off, ambil dari data store
                        _isAlreadyAbsen.value = prefsManager.getAbsenStatus.first()
                    }
            } finally {
                _isLoadingAbsen.value = false
            }
        }
    }

    fun submitAbsen(qrCode : String, lat: Double, long: Double) {
        viewModelScope.launch {
            _isLoadingAbsen.value = true

            try {
                val result = submitAttendanceUC.invoke(qrCode, lat, long)

                result.onSuccess {
                    _isAlreadyAbsen.value = true
                    println("DEBUG: Absen Berhasil (Logic by UseCase)")
                }.onFailure { e ->
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