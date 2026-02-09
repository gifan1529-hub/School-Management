package com.example.schoolmanagement.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolmanagement.Domain.Model.LogData
import com.example.schoolmanagement.Domain.UseCase.GetActivityLogUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ActivityLogViewModel (
    private val getActivityLogsUC: GetActivityLogUseCase,
): ViewModel() {
    private val _logs = MutableStateFlow<List<LogData>>(emptyList())
    val logs: StateFlow<List<LogData>> = _logs

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadLogs()
    }

    fun loadLogs(){
        viewModelScope.launch {
            _isLoading.value = true
            getActivityLogsUC().onSuccess { _logs.value = it }
            _isLoading.value = false
        }
    }
}