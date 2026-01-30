package com.example.schoolmanagement.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Data.Remote.ScheduleItem
import com.example.schoolmanagement.Domain.UseCase.GetScheduleUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ScheduleViewModel (
    private val getScheduleUseCase: GetScheduleUseCase,
    private val prefsManager: PrefsManager
): ViewModel() {

    private val _schedules = MutableStateFlow<List<ScheduleItem>>(emptyList())
    val schedules: StateFlow<List<ScheduleItem>> = _schedules

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadSchedules()
    }

    private fun loadSchedules() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = getScheduleUseCase.invoke()

            result.onSuccess {
                _schedules.value = it
            }.onFailure {
                println("DEEBUG: gagal jadwal: ${it.message}")
            }
            _isLoading.value = false
        }
    }

    val userClass: StateFlow<String> = prefsManager.getClass
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ""
        )
}