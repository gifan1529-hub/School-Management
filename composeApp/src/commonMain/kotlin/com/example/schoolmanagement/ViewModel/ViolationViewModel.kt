package com.example.schoolmanagement.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolmanagement.Data.Remote.StudentAttendance
import com.example.schoolmanagement.Domain.Model.ViolationData
import com.example.schoolmanagement.Domain.UseCase.GetTeacherDasboardUseCase
import com.example.schoolmanagement.Domain.UseCase.GetViolationUseCase
import com.example.schoolmanagement.Domain.UseCase.PostViolationUseCase
import com.example.schoolmanagement.Utils.HandleException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ViolationViewModel (
    private val getViolationUseCase: GetViolationUseCase,
    private val postViolationUseCase: PostViolationUseCase,
    private val getTeacherDasboardUseCase: GetTeacherDasboardUseCase
): ViewModel() {
    private val _studentList = MutableStateFlow<List<StudentAttendance>>(emptyList())
    val studentList: StateFlow<List<StudentAttendance>> = _studentList

    private val _violations = MutableStateFlow<List<ViolationData>>(emptyList())
    val violations: StateFlow<List<ViolationData>> = _violations

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isPostError = MutableStateFlow<String?>(null)
    val isPostError: StateFlow<String?> = _isPostError

    private val _isGetError = MutableStateFlow<String?>(null)
    val isGetError: StateFlow<String?> = _isGetError

    private val _isLoadError = MutableStateFlow<String?>(null)
    val isLoadError: StateFlow<String?> = _isLoadError

    val exceptionHandler = HandleException()

    fun loadStudents() {
        viewModelScope.launch {
            _isLoading.value = true
            _isLoadError.value = null
            getTeacherDasboardUseCase().onSuccess { data ->
                _studentList.value = data.studentList
            }.onFailure { e ->
                val handledError = exceptionHandler.handleException(e as Exception)
                _isLoadError.value = handledError.message
            }
            _isLoading.value = false
        }
    }

    fun loadViolations() {
        viewModelScope.launch {
            _isLoading.value = true
            _isGetError.value = null
            getViolationUseCase().onSuccess { data ->
                _violations.value = data
            }.onFailure { e ->
                val handledError = exceptionHandler.handleException(e as Exception)
                _isGetError.value = handledError.message
            }
            _isLoading.value = false
        }
    }

    fun submitViolation(
        studentId: Int,
        name: String,
        category: String,
        points: Int,
        description: String?,
        imageBytes: ByteArray?,
        imageName: String?
    ){
        viewModelScope.launch {
            _isLoading.value = true
            _isPostError.value = null
            _isSuccess.value = false

            val result = postViolationUseCase.invoke(
                studentId, name, category, points, description, imageBytes, imageName
            )

            result.onSuccess {
                _isSuccess.value = true
                loadViolations()
            }.onFailure { e ->
                val handledError = exceptionHandler.handleException(e as Exception)
                _isPostError.value = handledError.message
            }
        }
    }

    fun resetStatus(){
        _isSuccess.value = false
        _isPostError.value = null
        _isGetError.value = null
        _isSuccess.value = false
    }
}