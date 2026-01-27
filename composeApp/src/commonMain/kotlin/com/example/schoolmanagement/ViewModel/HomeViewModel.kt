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

    fun logout() {
        viewModelScope.launch {
            logoutUC.invoke()
            _logoutEvent.value = true
        }
    }

    private fun checkAndResetDailyAbsen() {
        viewModelScope.launch {
            val today = getTodayDate()
            val lastAbsenDate = prefsManager.getLastAbsenDate.first()

            if (today != lastAbsenDate) {
                // Jika tanggal berbeda, reset status absen di lokal
                prefsManager.saveAbsenStatus(false, today)
                _isAlreadyAbsen.value = false
            } else {
                // Jika tanggal sama, ambil status dari prefs
                _isAlreadyAbsen.value = prefsManager.getAbsenStatus.first()
            }
        }
    }

    fun submitAbsen(lat: Double, lon: Double) {
        viewModelScope.launch {
            _isLoadingAbsen.value = true
            val result = submitAttendanceUC.invoke(lat, lon)

            result.onSuccess {
                _isAlreadyAbsen.value = true
            }.onFailure {

            }
            _isLoadingAbsen.value = false
        }
    }
}