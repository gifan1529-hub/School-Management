package com.example.schoolmanagement.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolmanagement.Domain.Model.GradeDetailData
import com.example.schoolmanagement.Domain.Model.GradeSummaryData
import com.example.schoolmanagement.Domain.UseCase.GetGradeSummaryUseCase
import com.example.schoolmanagement.Domain.UseCase.GetMyGradesUseCase
import com.example.schoolmanagement.Utils.HandleException
import io.github.aakira.napier.Napier.e
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class GradeViewModel(
    private val getGradeSummaryUC: GetGradeSummaryUseCase,
    private val getMyGradesUC: GetMyGradesUseCase
) : ViewModel() {
    private val _grades = MutableStateFlow<GradeSummaryData?>(null)
    val grades: StateFlow<GradeSummaryData?> = _grades

    private val _myGradeDetails = MutableStateFlow<List<GradeDetailData>>(emptyList())
    val myGradeDetails: StateFlow<List<GradeDetailData>> = _myGradeDetails.asStateFlow()

    private val _errorLoadMessage = MutableStateFlow<String?>(null)
    val errorLoadMessage: StateFlow<String?> = _errorLoadMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val exceptionHandler = HandleException()

    init {
        loadGrades()
    }

    fun loadGrades() {
        viewModelScope.launch {
            _isLoading.value = true
            getGradeSummaryUC().onSuccess { _grades.value = it }
            _isLoading.value = false
        }
    }

    fun loadMyGrades(subject: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            getMyGradesUC(subject).onSuccess { data ->
                _myGradeDetails.value = data
            }.onFailure { e ->
                val handledError = exceptionHandler.handleException(e as Exception)
                _errorLoadMessage.value = handledError.message
                println("DEBUG GRADE: Gagal ambil detail nilai $handledError")
            }
            _isLoading.value = false
        }
    }

}