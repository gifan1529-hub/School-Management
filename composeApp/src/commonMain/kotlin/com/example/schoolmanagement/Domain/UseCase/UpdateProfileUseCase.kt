package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Data.Remote.UpdateProfileRequest
import com.example.schoolmanagement.Domain.Repository.UpdateProfileRepository
import kotlinx.coroutines.flow.first

class UpdateProfileUseCase (
    private val repository: UpdateProfileRepository,
    private val prefsManager: PrefsManager
) {
    suspend operator fun invoke(request: UpdateProfileRequest): Result<Boolean> {
        val token = prefsManager.getAuthToken.first() ?: ""
        return repository.updateProfile(token, request)
    }
}