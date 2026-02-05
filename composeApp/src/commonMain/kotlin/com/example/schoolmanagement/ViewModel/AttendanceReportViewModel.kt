package com.example.schoolmanagement.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolmanagement.Domain.Model.ReportSummary
import com.example.schoolmanagement.Domain.Model.StudentAttendanceItem
import com.example.schoolmanagement.Domain.UseCase.GetAttendanceReportUseCase
import com.example.schoolmanagement.Utils.HandleException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AttendanceReportViewModel(
    private val getAttendanceReportUseCase: GetAttendanceReportUseCase
): ViewModel() {
    private val _reportData = MutableStateFlow<List<StudentAttendanceItem>>(emptyList())
    val reportData: StateFlow<List<StudentAttendanceItem>> = _reportData

    private val _summary = MutableStateFlow(ReportSummary(0, 0, 0))
    val summary: StateFlow<ReportSummary> = _summary

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val exceptionHandler = HandleException()

    fun loadReport(
        role: String,
        className: String,
        status: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = getAttendanceReportUseCase(role, className, status)

            result.onSuccess { response ->
                _reportData.value = response.data
                _summary.value = response.summary
            }.onFailure { e ->
                val exception = exceptionHandler.handleException(e as Exception)
                _errorMessage.value = exception.message
            }
            _isLoading.value = false
        }
    }
}