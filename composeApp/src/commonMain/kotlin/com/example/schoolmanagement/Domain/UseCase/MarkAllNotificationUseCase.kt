package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Domain.Repository.ActivityLogRepository
import kotlinx.coroutines.flow.first

class MarkAllNotificationUseCase (
    private val repository: ActivityLogRepository,
    private val prefs: PrefsManager
) {
    suspend operator fun invoke(): Result<Boolean> {
        val token = prefs.getAuthToken.first() ?: ""
        return repository.markAllAsRead(token)
    }
}