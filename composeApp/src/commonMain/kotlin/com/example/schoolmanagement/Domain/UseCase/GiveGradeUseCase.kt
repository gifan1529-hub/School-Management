package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Domain.Repository.HomeWorkRepository
import kotlinx.coroutines.flow.first

class GiveGradeUseCase(
    private val repository: HomeWorkRepository,
    private val prefsManager: PrefsManager
) {
    suspend operator fun invoke(submissionId: Int, grade: Int): Result<Boolean> {
        return try {
            val token = prefsManager.getAuthToken.first() ?: ""
            if (token.isEmpty()) return Result.failure(Exception("Sesi berakhir"))

            repository.giveGrade(token, submissionId, grade)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}