package com.example.schoolmanagement.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolmanagement.DI.ToastHelper
import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Data.Remote.ApiService
import com.example.schoolmanagement.Domain.UseCase.LoginResult
import com.example.schoolmanagement.Domain.UseCase.LoginUC
import com.example.schoolmanagement.Domain.UseCase.UpdateFcmTokenUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SignInUiState(
    val emailValue: String = "",
    val passwordValue: String = "",
    val isLoading: Boolean = false
)

class SignIn (
    private val apiService: ApiService,
    private val loginUC: LoginUC,
    private val updateFcmTokenUC: UpdateFcmTokenUseCase,
    private val prefsManager: PrefsManager
): ViewModel() {
    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<LoginResult>()
    val eventFlow = _eventFlow.asSharedFlow()

    var toastMessage by mutableStateOf<String?>(null)

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(emailValue = email) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(passwordValue = password) }
    }

    fun onSignInClick(selectedRole: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = loginUC(_uiState.value.emailValue, _uiState.value.passwordValue)

            if (result is LoginResult.Success) {
                try {
                    // ngambil token dari hasil login
                    val token = result.response.accessToken

                    // Karena LoginResponse cuma isi token,
                    // manggil api getUser untuk dapat ID dan Email asli
                    val userProfile = apiService.getUser(token)

                    if (userProfile.role.equals(selectedRole, ignoreCase = true)) {
                        // nymprn sesi k data store
                        prefsManager.createLoginSession(
                            id = userProfile.id,
                            email = userProfile.email,
                            password = _uiState.value.passwordValue,
                            token = token,
                            name = userProfile.name,
                            role = userProfile.role,
                            nisn = userProfile.nisn,
                            kelas = userProfile.kelas,
                            phone = userProfile.phone,
                            address = userProfile.address
                        )
                        _eventFlow.emit(result)
                    } else {
                        _eventFlow.emit(LoginResult.Error("Role tidak sesuai"))
                        ToastHelper().Toast("Role Kamu bukan $selectedRole")
                        toastMessage = "Role Kamu bukan $selectedRole"
                    }

                    println("Profil : $token ${userProfile.id} ${userProfile.email}")
                } catch (e: Exception) {
                    _eventFlow.emit(LoginResult.Error("Gagal mengambil profil: ${e.message}"))
                }
            } else {
                _eventFlow.emit(result)
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun sendFcmToken(fcmToken: String) {
        viewModelScope.launch {
            updateFcmTokenUC(fcmToken).onSuccess {
                println("DEBUG FCM: BERHASIL KIRIM TOKEN KE LARAVEL -> $fcmToken")
            }.onFailure { e ->
                println("DEBUG FCM: Gagal lapor token: ${e.message}")
            }
        }
    }
}