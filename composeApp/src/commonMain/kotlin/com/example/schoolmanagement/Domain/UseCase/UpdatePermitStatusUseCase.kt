package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Domain.Repository.PermitRepository
import kotlinx.coroutines.flow.first

class UpdatePermitStatusUseCase(
    private val repository: PermitRepository,
    private val prefsManager: PrefsManager
) {
    suspend operator fun invoke(id: Int, status: String): Result<Boolean> {
        return try {
            val token = prefsManager.getAuthToken.first() ?: ""
            repository.updatePermitStatus(token, id, status)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}