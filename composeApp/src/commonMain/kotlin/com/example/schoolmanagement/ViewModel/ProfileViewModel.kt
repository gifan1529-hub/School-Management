package com.example.schoolmanagement.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Data.Remote.UpdateProfileRequest
import com.example.schoolmanagement.Domain.UseCase.LogoutUseCase
import com.example.schoolmanagement.Domain.UseCase.UpdateProfileUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel (
    private val prefsManager: PrefsManager,
    private val logoutUC: LogoutUseCase,
    private val updateProfileUC: UpdateProfileUseCase
): ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _updateResult = MutableSharedFlow<Result<Boolean>>()
    val updateResult = _updateResult.asSharedFlow()

    private val _logoutEvent = MutableStateFlow<Boolean>(false)
    val logoutEvent: StateFlow<Boolean> = _logoutEvent

    val userPhone: StateFlow<String> = prefsManager.getUserPhone
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "234"
        )

    val userRole: StateFlow<String> = prefsManager.getUserRole
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "Role"
        )

    val userEmail: StateFlow<String> = prefsManager.getUserEmail
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "Role"
        )

    val userName: StateFlow<String> = prefsManager.getUserName
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "Role"
        )

    fun logout() {
        viewModelScope.launch {
            logoutUC()
            _logoutEvent.value = true
        }
    }

    fun updateProfile(request: UpdateProfileRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            updateProfileUC(request).onSuccess {
                // Update data di lokal (DataStore) agar UI langsung berubah
                request.name?.let { prefsManager.saveUserName(it) }
                request.phone?.let { prefsManager.saveUserPhone(it) }
                request.`class`?.let { prefsManager.saveClass(it) }
                // Tambahkan save field lain jika ada di PrefsManager

                _updateResult.emit(Result.success(true))
            }.onFailure {
                _updateResult.emit(Result.failure(it))
            }
            _isLoading.value = false
        }
    }
}