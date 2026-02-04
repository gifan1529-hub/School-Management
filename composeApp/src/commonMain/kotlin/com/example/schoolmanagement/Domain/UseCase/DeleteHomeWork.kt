package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Domain.Repository.HomeWorkRepository
import kotlinx.coroutines.flow.first

class DeleteHomeWork(
    private val repository: HomeWorkRepository,
    private val prefsManager: PrefsManager
) {
    suspend operator fun invoke(homeWorkId: Int): Result<Boolean> {
        return try {
            val token = prefsManager.getAuthToken.first() ?: ""

            if (token.isEmpty()) {
                return Result.failure(Exception("Sesi berakhir, silakan login ulang"))
            }

            repository.deleteHomework(token, homeWorkId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}