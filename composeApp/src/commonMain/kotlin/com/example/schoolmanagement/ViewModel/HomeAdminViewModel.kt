package com.example.schoolmanagement.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolmanagement.Domain.Model.AdminStatsData
import com.example.schoolmanagement.Domain.Model.TrendData
import com.example.schoolmanagement.Domain.UseCase.GetAdminStatsUseCase
import com.example.schoolmanagement.Domain.UseCase.GetAttendanceTrendUseCase
import com.example.schoolmanagement.Utils.HandleException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class HomeAdminViewModel(
    private val getAdminStatsUseCase: GetAdminStatsUseCase,
    private val getAttendanceTrendUseCase: GetAttendanceTrendUseCase
): ViewModel() {

    private val _stats = MutableStateFlow(AdminStatsData("0", "0", "0"))
    val stats: StateFlow<AdminStatsData> = _stats

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _errorTrend = MutableStateFlow<String?>(null)
    val errorTrend: StateFlow<String?> = _errorTrend
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing
    private val _trendData = MutableStateFlow<List<TrendData>>(emptyList())
    val trendData: StateFlow<List<TrendData>> = _trendData


    private val exceptionHandler = HandleException()

    init {
        loadStats()
    }

    fun refreshData() {
        viewModelScope.launch {
            _isRefreshing.value = true
            // Jalankan ulang fungsi sinkronisasi data
            loadStats()
            // Beri sedikit delay agar animasi tidak terlalu cepat hilang
            delay(1000)
            _isRefreshing.value = false
        }
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

    fun loadAttendanceTrend(){
        viewModelScope.launch {
            _isLoading.value = true
            getAttendanceTrendUseCase().onSuccess {
                println("DEBUG ATTEND: $it")
                _trendData.value = it
            }.onFailure { e ->
                val handledError = exceptionHandler.handleException(e as Exception)
                _errorTrend.value = handledError.message
                println("DEBUG ATTEND: ${handledError.message}")
            }
            _isLoading.value = false
        }
    }
}