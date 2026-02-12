package com.example.schoolmanagement.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolmanagement.Data.Remote.ScheduleItem
import com.example.schoolmanagement.Domain.UseCase.GetAdminTeacherScheduleUseCase
import com.example.schoolmanagement.Utils.HandleException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminTeacherViewModel (
    private val getAdminTeacherScheduleUseCase: GetAdminTeacherScheduleUseCase,
): ViewModel() {
    private val _schedules = MutableStateFlow<List<ScheduleItem>>(emptyList())
    val schedules: StateFlow<List<ScheduleItem>> = _schedules.asStateFlow()

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorLoadSchedule = MutableStateFlow<String?>(null)
    val errorLoadSchedule : StateFlow<String?> = _errorLoadSchedule

    private val exceptionHandler = HandleException()

    init {
        loadSchedules()
    }

    fun loadSchedules(teacherId: Int? = null){
        viewModelScope.launch {
            _isLoading.value = true
            _errorLoadSchedule.value = null
            _isSuccess.value = false

            val result = getAdminTeacherScheduleUseCase.invoke(teacherId)

            result.onSuccess { data ->
                _schedules.value = data
                _isSuccess.value = true
                println("DEBUG: berhasil ambil jadwal")
            }.onFailure { e ->
                val handledError = exceptionHandler.handleException(e as Exception)
                _errorLoadSchedule.value = handledError.message
                println("DEBUG: gagal ambil jadwal: ${handledError.message}")
            }
            _isLoading.value = false
        }
    }
}