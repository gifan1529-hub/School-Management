package com.example.schoolmanagement.Domain.UseCase

import androidx.datastore.preferences.core.Preferences
import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Data.Remote.AlertRequest
import com.example.schoolmanagement.Data.Remote.AlertResponse
import com.example.schoolmanagement.Domain.Repository.AnnouncmentRepository
import kotlinx.coroutines.flow.first

class PostAnnouncmentUseCase (
    private val repository: AnnouncmentRepository,
    private val prefsManager: PrefsManager
) {
    suspend operator fun invoke(title: String, message: String, type: String, audience: String): Result<AlertResponse> {
        return try {
            val token = prefsManager.getAuthToken.first() ?: ""

            if (token.isEmpty()) return Result.failure(Exception("Sesi berakhir, silakan login ulang"))
            if (title.isBlank() || message.isBlank()) return Result.failure(Exception("Judul dan pesan tidak boleh kosong"))

            val request = AlertRequest(
                title = title,
                message = message,
                type = type,
                audience = audience
            )

            repository.postAnnouncement(token, request)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}