package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Data.Remote.AddUserRequest
import com.example.schoolmanagement.Domain.Repository.UpdateUserRepository
import kotlinx.coroutines.flow.first

class AddUserUseCase (
    private val repository: UpdateUserRepository,
    private val prefs: PrefsManager
) {
    suspend operator fun invoke(request: AddUserRequest): Result<Boolean> {
        val token = prefs.getAuthToken.first() ?: ""
        return repository.addUser(token, request)
    }
}