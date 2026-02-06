package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Domain.Repository.UpdateUserRepository
import kotlinx.coroutines.flow.first

class UpdateUserUseCase(
    private val repository: UpdateUserRepository,
    private val prefsManager: PrefsManager
) {
    suspend operator fun invoke(userId: Int, role: String): Result<Boolean> {
        val token = prefsManager.getAuthToken.first() ?: ""
        return repository.updateUser(token, userId, role)
    }
}