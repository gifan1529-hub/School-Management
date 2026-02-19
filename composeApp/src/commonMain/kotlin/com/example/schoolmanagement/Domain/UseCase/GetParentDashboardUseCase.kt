package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Domain.Model.ParentDashboardData
import com.example.schoolmanagement.Domain.Repository.ParentRepository
import kotlinx.coroutines.flow.first

class GetParentDashboardUseCase(
    private val repository: ParentRepository,
    private val prefsManager: PrefsManager
) {
    suspend operator fun invoke(): Result<ParentDashboardData> {
        val token = prefsManager.getAuthToken.first() ?: ""
        return repository.getDashboard(token)
    }
}