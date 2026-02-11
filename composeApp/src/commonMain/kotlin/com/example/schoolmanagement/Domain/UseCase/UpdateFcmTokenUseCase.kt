package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Domain.Repository.AuthRepository
import kotlinx.coroutines.flow.first

class UpdateFcmTokenUseCase (
    private val repository: AuthRepository,
    private val prefsManager: PrefsManager
) {
    suspend operator fun invoke(fcmToken: String): Result<Boolean> {
        val userToken = prefsManager.getAuthToken.first() ?: ""
        if (userToken.isEmpty()) return Result.failure(Exception("Token empty"))

        return repository.updateFcmToken(userToken, fcmToken)
    }
}