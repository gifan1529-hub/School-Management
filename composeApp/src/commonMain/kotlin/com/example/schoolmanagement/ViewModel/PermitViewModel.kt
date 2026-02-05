package com.example.schoolmanagement.ViewModel

import androidx.compose.ui.semantics.role
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolmanagement.DI.ToastHelper
import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Domain.Model.PermitData
import com.example.schoolmanagement.Domain.Repository.PermitRepository
import com.example.schoolmanagement.Domain.UseCase.GetPermitHistoryUseCase
import com.example.schoolmanagement.Domain.UseCase.SubmitPermitUseCase
import com.example.schoolmanagement.Domain.UseCase.UpdatePermitStatusUseCase
import com.example.schoolmanagement.Utils.HandleException
import com.example.schoolmanagement.getTodayDateS
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.text.lowercase

class PermitViewModel(
    private val repository: PermitRepository,
    private val submitPermitUseCase: SubmitPermitUseCase,
    private val getPermitHistoryUC: GetPermitHistoryUseCase,
    private val updatePermitStatusUC: UpdatePermitStatusUseCase,
    private val prefsManager: PrefsManager
): ViewModel() {
    private val _tipeIzin = MutableStateFlow("Sakit")
    val tipeIzin: StateFlow<String> = _tipeIzin

    private val _tanggalMulai = MutableStateFlow("")
    val tanggalMulai: StateFlow<String> = _tanggalMulai

    private val _tanggalSelesai = MutableStateFlow("")
    val tanggalSelesai: StateFlow<String> = _tanggalSelesai

    private val _jamMulai = MutableStateFlow("")
    val jamMulai: StateFlow<String> = _jamMulai

    private val _jamSelesai = MutableStateFlow("")
    val jamSelesai: StateFlow<String> = _jamSelesai

    private val _alasan = MutableStateFlow("")
    val alasan: StateFlow<String> = _alasan

    private val _myPermitHistory = MutableStateFlow<List<PermitData>>(emptyList())
    val  myPermitHistory: StateFlow<List<PermitData>> = _myPermitHistory

    private val _muridPermitHistory = MutableStateFlow<List<PermitData>>(emptyList())
    val muridPermitHistory: StateFlow<List<PermitData>> = _muridPermitHistory

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    private val _errorSubmitMessage = MutableStateFlow<String?>(null)
    val errorSubmitMessage: StateFlow<String?> = _errorSubmitMessage

    private val _errorUpdateMessage = MutableStateFlow<String?>(null)
    val errorUpdateMessage: StateFlow<String?> = _errorUpdateMessage

    private val exceptionHandler = HandleException()

    fun onTipeChange(newType: String) { _tipeIzin.value = newType }
    fun onStartDateChange(date: String) { _tanggalMulai.value = date }
    fun onEndDateChange(date: String) { _tanggalSelesai.value = date }
    fun onTimeInChange(time: String) { _jamMulai.value = time }
    fun onTimeOutChange(time: String) { _jamSelesai.value = time }
    fun onReasonChange(text: String) { _alasan.value = text }

    fun submitPermit() {
        viewModelScope.launch {
            if (_alasan.value.isBlank() || _jamMulai.value.isBlank()) return@launch

            _isLoading.value = true
            val result = submitPermitUseCase.invoke(
                type = _tipeIzin.value,
                startDate = _tanggalMulai.value,
                endDate = _tanggalSelesai.value,
                reason = _alasan.value,
                tIn = _jamMulai.value,
                tOut = _jamSelesai.value
            )

            result.onSuccess {
                _isSuccess.value = true
            }.onFailure { e ->
                val handledError = exceptionHandler.handleException(e as Exception)
                _errorSubmitMessage.value = handledError.message
                _isSuccess.value = false
            }
            _isLoading.value = false
        }
    }

    fun loadPermitHistory() {
        viewModelScope.launch {
            _isLoading.value = true
            val token = prefsManager.getAuthToken.first() ?: ""
            repository.getPermits(token, all = false).onSuccess {
                _myPermitHistory.value = it
            }
            val role = prefsManager.getUserRole.first()
            if (role.lowercase() == "teacher") {
                repository.getPermits(token, all = true).onSuccess {
                    _muridPermitHistory.value = it
                }
            }
            _isLoading.value = false
        }
    }

    fun updatePermitStatus(permitId: Int, newStatus: String){
        viewModelScope.launch {
            _isLoading.value = true
            val result = updatePermitStatusUC.invoke(permitId, newStatus)

            result.onSuccess {
                _isSuccess.value = true
                loadPermitHistory()
            }.onFailure { e ->
                val handledError = exceptionHandler.handleException(e as Exception)
                _errorUpdateMessage.value = handledError.message
                _isSuccess.value = false
            }
            _isLoading.value = false
        }
    }
}