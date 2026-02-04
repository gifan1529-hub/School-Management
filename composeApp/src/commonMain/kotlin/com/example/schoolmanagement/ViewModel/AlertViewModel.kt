package com.example.schoolmanagement.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolmanagement.Domain.Model.AlertData
import com.example.schoolmanagement.Domain.UseCase.GetAnnouncmentUseCase
import com.example.schoolmanagement.Domain.UseCase.PostAnnouncmentUseCase
import com.example.schoolmanagement.Utils.HandleException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AlertViewModel (
    private val postAnnouncementUC: PostAnnouncmentUseCase,
    private val getAnnouncmentUseCase: GetAnnouncmentUseCase
): ViewModel() {
    private val _alerts = MutableStateFlow<List<AlertData>>(emptyList())
    val alerts: StateFlow<List<AlertData>> = _alerts

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val exceptionHandler = HandleException()

    init {
        loadAnnouncements()
    }

    fun loadAnnouncements() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = getAnnouncmentUseCase()
            result.onSuccess { data ->
                println("DEBUG ALERT: Berhasil ambil ${data.size} data")
                _alerts.value = data
            }
            result.onFailure { e ->
                val handledError = exceptionHandler.handleException(e as Exception)
                _errorMessage.value = handledError.message
            }
            _isLoading.value = false
        }
    }

    fun createAnnouncement(title: String, message: String, type: String) {
        viewModelScope.launch {
            if (title.isBlank() || message.isBlank()) {
                _errorMessage.value = "Judul dan Pesan wajib diisi"
                return@launch
            }

            _isLoading.value = true
            val result = postAnnouncementUC(title, message, type)

            result.onSuccess {
                _isSuccess.value = true
                loadAnnouncements()
            }.onFailure { e ->
                val handledError = exceptionHandler.handleException(e as Exception)
                _errorMessage.value = handledError.message
            }
            _isLoading.value = false
        }
    }

    fun resetSuccessState() {
        _isSuccess.value = false
    }
}