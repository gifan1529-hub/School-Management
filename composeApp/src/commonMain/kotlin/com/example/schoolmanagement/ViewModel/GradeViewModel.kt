package com.example.schoolmanagement.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolmanagement.Domain.Model.GradeSummaryData
import com.example.schoolmanagement.Domain.UseCase.GetGradeSummaryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class GradeViewModel(
    private val getGradeSummaryUC: GetGradeSummaryUseCase
) : ViewModel() {
    private val _grades = MutableStateFlow<GradeSummaryData?>(null)
    val grades: StateFlow<GradeSummaryData?> = _grades

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

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
}