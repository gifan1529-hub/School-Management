package com.example.schoolmanagement.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Domain.UseCase.SubmitAttendanceUC
import com.example.schoolmanagement.Domain.UseCase.UserDetails
import com.example.schoolmanagement.Domain.UseCase.getDetailUserUC
import com.example.schoolmanagement.Domain.UseCase.LogoutUseCase
import com.example.schoolmanagement.getTodayDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel (
    private val prefsManager: PrefsManager,
    private val logoutUC: LogoutUseCase,
    private val getDetailUserUC: getDetailUserUC,
    private val submitAttendanceUC: SubmitAttendanceUC
): ViewModel() {
    private val _userDetails = MutableStateFlow<UserDetails?>(null)
    val userDetails: StateFlow<UserDetails?> = _userDetails

    private val _isAlreadyAbsen = MutableStateFlow(false)
    val isAlreadyAbsen: StateFlow<Boolean> = _isAlreadyAbsen

    private val _isLoadingAbsen = MutableStateFlow(false)
    val isLoadingAbsen: StateFlow<Boolean> = _isLoadingAbsen

    private val _logoutEvent = MutableStateFlow(false)
    val logoutEvent: StateFlow<Boolean> = _logoutEvent

    init {
        loadUserDetail()
        viewModelScope.launch {
            // cek dan reset absen setiap hari
            val today = getTodayDate()
            val lastAbsenDate = prefsManager.getLastAbsenDate.first()

            if (lastAbsenDate != null && today != lastAbsenDate) {
                // Jika tanggal berbeda, reset status absen
                prefsManager.saveAbsenStatus(false, today)
            } else if (lastAbsenDate == null) {
                prefsManager.saveAbsenStatus(false, today)
            }

            prefsManager.getAbsenStatus.collect { status ->
                _isAlreadyAbsen.value = status
                println("DEBUG: Status Absen Terbaru: $status")
            }
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

//    private fun checkAndResetDailyAbsen() {
//        viewModelScope.launch {
//            val today = getTodayDate()
//            val lastAbsenDate = prefsManager.getLastAbsenDate.first()
//
//            if (today != lastAbsenDate) {
//                // Jika tanggal berbeda, reset status absen di lokal
//                prefsManager.saveAbsenStatus(false, today)
//            } else {
//                // Jika tanggal sama, ambil status dari prefs
//                _isAlreadyAbsen.value = prefsManager.getAbsenStatus.first()
//            }
//        }
//    }

    fun submitAbsen(qrCode : String) {
        viewModelScope.launch {
            _isLoadingAbsen.value = true

            try {
                val token = prefsManager.getAuthToken.first() ?: ""
                val result = submitAttendanceUC.invoke(qrCode, token)

                result.onSuccess {
                    val today = getTodayDate()
                    prefsManager.saveAbsenStatus(true, today)
                    _isAlreadyAbsen.value = true
                    println("DEBUG: Absen Berhasil Disimpan ke Prefs")
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