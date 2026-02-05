package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Domain.Model.TrendData
import com.example.schoolmanagement.Domain.Repository.StatsRepository
import kotlinx.coroutines.flow.first

class GetAttendanceTrendUseCase (
    private val repository: StatsRepository,
    private val prefsManager: PrefsManager
) {
    suspend operator fun invoke(): Result<List<TrendData>> {
        val token = prefsManager.getAuthToken.first() ?: ""
        return repository.getAttendanceTrend(token)
    }
}