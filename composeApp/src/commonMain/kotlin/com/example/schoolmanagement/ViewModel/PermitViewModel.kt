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

    private val _fileName = MutableStateFlow("")
    val fileName: StateFlow<String> = _fileName

    private val _fileBytes = MutableStateFlow<ByteArray?>(null)
    val fileBytes: StateFlow<ByteArray?> = _fileBytes

    private val _alasan = MutableStateFlow("")
    val alasan: StateFlow<String> = _alasan

    private val _myPermitHistory = MutableStateFlow<List<PermitData>>(emptyList())
    val  myPermitHistory: StateFlow<List<PermitData>> = _myPermitHistory

    private val _guruPermitHistory = MutableStateFlow<List<PermitData>>(emptyList())
    val guruPermitHistory: StateFlow<List<PermitData>> = _guruPermitHistory

    private val _allMuridPermitHistory = MutableStateFlow<List<PermitData>>(emptyList())
    val allMuridPermitHistory: StateFlow<List<PermitData>> = _allMuridPermitHistory

    private val _muridPermitHistory = MutableStateFlow<List<PermitData>>(emptyList())
    val muridPermitHistory: StateFlow<List<PermitData>> = _muridPermitHistory

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    private val _isUpdateSuccess = MutableStateFlow(false)
    val isUpdateSuccess: StateFlow<Boolean> = _isUpdateSuccess

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
    fun onFileSelected(name: String, bytes: ByteArray) {
        _fileName.value = name
        _fileBytes.value = bytes
    }

    fun submitPermit() {
        viewModelScope.launch {
            _isSuccess.value = false
            _errorSubmitMessage.value = null

            if (
                _tipeIzin.value.isBlank() ||
                _alasan.value.isBlank() ||
                _jamMulai.value.isBlank() ||
                _jamSelesai.value.isBlank() ||
                _tanggalMulai.value.isBlank() ||
                _tanggalSelesai.value.isBlank()
            ) {
                ToastHelper().Toast("Mohon isi semua data")
                return@launch
            }
                _isLoading.value = true
                val result = submitPermitUseCase.invoke(
                    type = _tipeIzin.value,
                    startDate = _tanggalMulai.value,
                    endDate = _tanggalSelesai.value,
                    reason = _alasan.value,
                    tIn = _jamMulai.value,
                    tOut = _jamSelesai.value,
                    fileName = _fileName.value,
                    fileBytes = _fileBytes.value
                )

                result.onSuccess {
                    println("DEBUG PERMIT: berahsil submit izin")
                    _isSuccess.value = true
                }.onFailure { e ->
                    println("DEBUG PERMIT: gagal submit izin")
                    val handledError = exceptionHandler.handleException(e as Exception)
                    _errorSubmitMessage.value = handledError.message
                    println("DEBUG PERMIT: ${handledError.message}")
//                    ToastHelper().Toast(handledError.message)
                    _isSuccess.value = false
                }
                _isLoading.value = false
        }
    }

    fun loadPermitHistory() {
        viewModelScope.launch {
            _isLoading.value = true
            val token = prefsManager.getAuthToken.first() ?: ""
            val role = prefsManager.getUserRole.first()

            repository.getPermits(token, all = false).onSuccess {
                _myPermitHistory.value = it
            }
            if (role.lowercase() == "teacher" || role.lowercase() == "admin") {
                repository.getPermits(token, all = true).onSuccess { allData ->
                    if (role.lowercase() == "admin") {
                        _guruPermitHistory.value = allData.filter { it.user?.role?.lowercase() == "teacher" }
                        _allMuridPermitHistory.value = allData.filter { it.user?.role?.lowercase() == "student" }
                    } else {
                        _muridPermitHistory.value = allData
                    }
                }
            }
            _isLoading.value = false
        }
    }

    fun updatePermitStatus(permitId: Int, newStatus: String){
        viewModelScope.launch {
            _isLoading.value = true
            _isUpdateSuccess.value = false
            val result = updatePermitStatusUC.invoke(permitId, newStatus)

            result.onSuccess {
                _isUpdateSuccess.value = true
                loadPermitHistory()
            }.onFailure { e ->
                val handledError = exceptionHandler.handleException(e as Exception)
                _errorUpdateMessage.value = handledError.message
                _isUpdateSuccess.value = false
            }
            _isLoading.value = false
        }
    }
}