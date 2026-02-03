package com.example.schoolmanagement.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolmanagement.Data.Remote.ScheduleItem
import com.example.schoolmanagement.Domain.UseCase.GetTeacherSchedulesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ScheduleGuruViewModel (
    private val getTeacherSchedulesUseCase: GetTeacherSchedulesUseCase,
): ViewModel() {
    private val _schedules = MutableStateFlow<List<ScheduleItem>>(emptyList())
    val schedules: StateFlow<List<ScheduleItem>> = _schedules

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        loadSchedules()
    }

    fun loadSchedules(){
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = getTeacherSchedulesUseCase.invoke()
            result.onSuccess { data ->
                _schedules.value = data
            }.onFailure { e ->
                _errorMessage.value = e.message
            }
            _isLoading.value = false
        }
    }
}