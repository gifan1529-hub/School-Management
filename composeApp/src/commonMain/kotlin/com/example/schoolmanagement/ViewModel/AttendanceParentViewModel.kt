package com.example.schoolmanagement.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolmanagement.Data.Remote.ChildAttendanceResponse
import com.example.schoolmanagement.Domain.UseCase.GetChildAttendanceUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Month

class AttendanceParentViewModel (
    private val getAttendanceUC: GetChildAttendanceUseCase
): ViewModel() {
    private val _state = MutableStateFlow<ChildAttendanceResponse?>(null)
    val state = _state.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun loadAttendance(month: Month, year: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            getAttendanceUC(month, year).onSuccess {
                _state.value = it
            }
            _isLoading.value = false
        }
    }
}