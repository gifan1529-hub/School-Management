package com.example.schoolmanagement.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolmanagement.DI.ToastHelper
import com.example.schoolmanagement.Data.Remote.AddUserRequest
import com.example.schoolmanagement.Domain.Model.UserDatas
import com.example.schoolmanagement.Domain.UseCase.AddUserUseCase
import com.example.schoolmanagement.Domain.UseCase.DeleteUserUseCase
import com.example.schoolmanagement.Domain.UseCase.GetAllUserUseCase
import com.example.schoolmanagement.Domain.UseCase.UpdateUserUseCase
import com.example.schoolmanagement.Utils.HandleException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UpdateUserViewModel (
    private val updateUserUseCase: UpdateUserUseCase,
    private val getAllUsersUseCase: GetAllUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val addUsersUseCase: AddUserUseCase
    ): ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    private val _isAddSuccess = MutableStateFlow(false)
    val isAddSuccess: StateFlow<Boolean> = _isAddSuccess

    private val _allUsers = MutableStateFlow<List<UserDatas>>(emptyList())
    val allUsers: StateFlow<List<UserDatas>> = _allUsers.asStateFlow()

    private val _errorUpdateMessage = MutableStateFlow<String?>(null)
    val errorUpdateMessage: StateFlow<String?> = _errorUpdateMessage

    private val _errorAddMessage = MutableStateFlow<String?>(null)
    val errorAddMessage: StateFlow<String?> = _errorAddMessage

    private val _errorLoadMessage = MutableStateFlow<String?>(null)
    val errorLoadMessage: StateFlow<String?> = _errorLoadMessage

    private val _errorDeleteMessage = MutableStateFlow<String?>(null)
    val errorDeleteMessage: StateFlow<String?> = _errorDeleteMessage

    private val exceptionHandler = HandleException()

    var toastMessage by mutableStateOf<String?>(null)

    init {
        loadAllUsers()
    }
    fun loadAllUsers(role: String? = null, search: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            getAllUsersUseCase(role, search).onSuccess {
                _allUsers.value = it
            }.onFailure { e: Throwable ->
                _errorLoadMessage.value = e.message
            }
            _isLoading.value = false
        }
    }

    fun deleteUser(userId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            deleteUserUseCase(userId).onSuccess {
                loadAllUsers()
            }.onFailure { e: Throwable ->
                _errorDeleteMessage.value = e.message
            }
            _isLoading.value = false
        }
    }

    fun updateUser(userId: Int, role: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorUpdateMessage.value = null

            updateUserUseCase.invoke(userId, role)
                .onSuccess {
                    _isSuccess.value = true
                }
                .onFailure { e ->
                    val handledError = exceptionHandler.handleException(e as Exception)
                    _errorUpdateMessage.value = handledError.message
                }
            _isLoading.value = false
        }
    }

    fun registerUser(request: AddUserRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorAddMessage.value = null

            addUsersUseCase.invoke(request)
                .onSuccess {
                    _isAddSuccess.value = true
                    ToastHelper().Toast("Berhasil menambahkan user")
                    loadAllUsers()
                }.onFailure { e ->
                    val handledError = exceptionHandler.handleException(e as Exception)
                    _errorAddMessage.value = handledError.message
                }
            _isLoading.value = false
            }
        }
}