package com.example.schoolmanagement.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolmanagement.Domain.Model.LogData
import com.example.schoolmanagement.Domain.UseCase.GetActivityLogUseCase
import com.example.schoolmanagement.Domain.UseCase.GetUnreadNotifActivityUseCase
import com.example.schoolmanagement.Domain.UseCase.MarkAllNotificationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class ActivityLogViewModel (
    private val getActivityLogsUC: GetActivityLogUseCase,
    private val getUnreadNotificationUC: GetUnreadNotifActivityUseCase,
    private val markAllNotificationUC: MarkAllNotificationUseCase
): ViewModel() {
    private val _logs = MutableStateFlow<List<LogData>>(emptyList())
    val logs: StateFlow<List<LogData>> = _logs
    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadLogs()
        loadUnreadCount()
    }

    fun loadLogs(){
        viewModelScope.launch {
            _isLoading.value = true
            getActivityLogsUC().onSuccess { _logs.value = it }
            _isLoading.value = false
        }
    }

    fun loadUnreadCount(){
        viewModelScope.launch {
            getUnreadNotificationUC().onSuccess { count ->
                _unreadCount.value = count
            }
        }
    }

    fun markAsRead(){
        viewModelScope.launch {
            markAllNotificationUC()
            _unreadCount.value = 0
        }
    }
}