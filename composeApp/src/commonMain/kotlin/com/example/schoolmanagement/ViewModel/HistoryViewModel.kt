package com.example.schoolmanagement.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolmanagement.Data.Remote.AttendanceRecord
import com.example.schoolmanagement.Domain.UseCase.GetAttendanceHistoryUseCase
import com.example.schoolmanagement.Utils.HandleException
import io.github.aakira.napier.Napier.e
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

class HistoryViewModel (
    private val getHistoryUC : GetAttendanceHistoryUseCase
): ViewModel() {

    private val _history = MutableStateFlow<List<AttendanceRecord>>(emptyList())
    val history: StateFlow<List<AttendanceRecord>> = _history

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val exceptionHandler = HandleException()

    init {
        loadHistory()
    }

    fun loadHistory() {
        viewModelScope.launch {
            _isLoading.value = true
            getHistoryUC.invoke()
                .onSuccess { _history.value = it }
                .onFailure { e ->
                    val handledError = exceptionHandler.handleException(e as Exception)
                    _errorMessage.value = handledError.message
                }
            _isLoading.value = false
        }
    }
}