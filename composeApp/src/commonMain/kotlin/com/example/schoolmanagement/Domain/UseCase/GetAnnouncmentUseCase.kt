package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Domain.Model.AlertData
import com.example.schoolmanagement.Domain.Repository.AnnouncmentRepository
import kotlinx.coroutines.flow.first

class GetAnnouncmentUseCase (
    private val repository: AnnouncmentRepository,
    private val prefsManager: PrefsManager
) {
    suspend operator fun invoke(): Result<List<AlertData>> {
        return try {
            val token = prefsManager.getAuthToken.first() ?: ""

            if (token.isEmpty()) {
                return Result.failure(Exception("Sesi berakhir, silakan login ulang"))
            }

            repository.getAnnouncements(token)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}