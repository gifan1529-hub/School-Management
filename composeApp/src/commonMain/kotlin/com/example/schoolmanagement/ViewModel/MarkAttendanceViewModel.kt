package com.example.schoolmanagement.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Data.Remote.StudentAttendance
import com.example.schoolmanagement.Domain.UseCase.GetTeacherDasboardUseCase
import com.example.schoolmanagement.Utils.HandleException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MarkAttendanceViewModel (
    private val getTeacherDashboardUC: GetTeacherDasboardUseCase
): ViewModel() {
    private val _studentList = MutableStateFlow<List<StudentAttendance>>(emptyList())
    val studentList: StateFlow<List<StudentAttendance>> = _studentList

    private val _countHadir = MutableStateFlow("0")
    val countHadir: StateFlow<String> = _countHadir

    private val _countAbsen = MutableStateFlow("0")
    val countAbsen: StateFlow<String> = _countAbsen

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val exceptionHandler = HandleException()

    init {
        loadStudentAttendance()
    }

    fun loadStudentAttendance() {
        viewModelScope.launch {
            getTeacherDashboardUC.invoke()
                .onSuccess { stats ->
                    _studentList.value = stats.studentList
                    _countHadir.value = stats.hadir
                    _countAbsen.value = stats.absen
                }
                .onFailure { e ->
                    val handledError = exceptionHandler.handleException(e as Exception)
                    _errorMessage.value = handledError.message
                }
        }
    }
}