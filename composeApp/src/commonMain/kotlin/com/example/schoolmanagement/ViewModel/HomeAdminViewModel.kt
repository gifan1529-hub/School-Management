package com.example.schoolmanagement.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolmanagement.Domain.Model.AdminStatsData
import com.example.schoolmanagement.Domain.UseCase.GetAdminStatsUseCase
import com.example.schoolmanagement.Utils.HandleException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class HomeAdminViewModel(
    private val getAdminStatsUseCase: GetAdminStatsUseCase,
): ViewModel() {

    private val _stats = MutableStateFlow(AdminStatsData("0", "0", "0"))
    val stats: StateFlow<AdminStatsData> = _stats

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val exceptionHandler = HandleException()

    init {
        loadStats()
    }

    fun loadStats(){
        viewModelScope.launch {
            _isLoading.value = true
            getAdminStatsUseCase().onSuccess {
                _stats.value = it
            }.onFailure { e ->
                val handledError = exceptionHandler.handleException(e as Exception)
                _error.value = handledError.message
            }
            _isLoading.value = false
        }
    }
}