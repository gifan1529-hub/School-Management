package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Domain.Repository.UpdateUserRepository
import kotlinx.coroutines.flow.first

class DeleteUserUseCase (
    private val repository: UpdateUserRepository,
    private val prefs: PrefsManager
) {
    suspend operator fun invoke(userId: Int): Result<Boolean> {
        val token = prefs.getAuthToken.first() ?: ""
        return repository.deleteUser(token, userId)
    }

}