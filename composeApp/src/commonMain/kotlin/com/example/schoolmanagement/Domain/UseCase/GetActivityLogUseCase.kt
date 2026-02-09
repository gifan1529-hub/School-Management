package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Domain.Model.LogData
import com.example.schoolmanagement.Domain.Repository.ActivityLogRepository
import kotlinx.coroutines.flow.first

class GetActivityLogUseCase (
    private val repository: ActivityLogRepository,
    private val prefs: PrefsManager
) {
    suspend operator fun invoke(): Result<List<LogData>> {
        val token = prefs.getAuthToken.first() ?: ""
        val role = prefs.getUserRole.first() ?: ""
        val isAdmin = role.equals("admin", ignoreCase = true)

        return repository.getActivityLogs(token, isAdmin)
    }
}