package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Domain.Repository.HomeWorkRepository
import kotlinx.coroutines.flow.first

class SubmitHomeworkUseCase(
    private val repository: HomeWorkRepository,
    private val prefsManager: PrefsManager
) {
    suspend operator fun invoke (
        homeworkId: Int,
        fileBytes: ByteArray,
        fileName: String
    ): Result<Boolean> {
        return try {
            val token = prefsManager.getAuthToken.first() ?: ""
            if (token.isEmpty()) return Result.failure(Exception("Sesi berakhir"))

            repository.submitHomework(token, homeworkId, fileBytes, fileName)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}