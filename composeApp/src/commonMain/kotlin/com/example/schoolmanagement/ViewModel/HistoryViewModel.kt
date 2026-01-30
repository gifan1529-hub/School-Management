package com.example.schoolmanagement.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolmanagement.Data.Remote.AttendanceRecord
import com.example.schoolmanagement.Domain.UseCase.GetAttendanceHistoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

class HistoryViewModel (
    private val getHistoryUC : GetAttendanceHistoryUseCase
): ViewModel() {

    private val _history = MutableStateFlow<List<AttendanceRecord>>(emptyList())
    val history: StateFlow<List<AttendanceRecord>> = _history

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadHistory()
    }

    fun loadHistory() {
        viewModelScope.launch {
            _isLoading.value = true
            getHistoryUC.invoke()
                .onSuccess { _history.value = it }
                .onFailure {  }
            _isLoading.value = false
        }
    }
}