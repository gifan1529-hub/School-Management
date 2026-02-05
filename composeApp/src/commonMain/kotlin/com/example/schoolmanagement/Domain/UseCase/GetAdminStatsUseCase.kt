package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Domain.Model.AdminStatsData
import com.example.schoolmanagement.Domain.Repository.StatsRepository
import kotlinx.coroutines.flow.first

class GetAdminStatsUseCase (
    private val repository: StatsRepository,
    private val prefs: PrefsManager
) {
    suspend operator fun invoke(): Result<AdminStatsData>{
        val token = prefs.getAuthToken.first() ?: ""
        return repository.getStats(token)
    }
}