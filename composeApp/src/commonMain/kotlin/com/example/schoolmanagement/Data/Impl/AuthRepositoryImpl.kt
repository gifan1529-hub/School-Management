package com.example.schoolmanagement.Data.Impl

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Data.Remote.ApiService
import com.example.schoolmanagement.Domain.Repository.AuthRepository
import com.example.schoolmanagement.Utils.HandleException
import kotlinx.coroutines.flow.first

class AuthRepositoryImpl (
    private val apiService: ApiService,
    private val prefsManager: PrefsManager
): AuthRepository {
    private val exceptionHandler = HandleException()

    override suspend fun updateFcmToken(token: String, fcmToken: String): Result<Boolean> {
    return try {
        apiService.sendFcmToken(token, fcmToken)
        Result.success(true)
    } catch (e: Exception) {
        Result.failure(exceptionHandler.handleException(e))
    }
    }

    override suspend fun logout(): Result<Boolean> {
        return try {
            val token = prefsManager.getAuthToken.first() ?: ""

            if (token.isNotEmpty()) {
                apiService.logout(token)
            }
            prefsManager.clearSession()
            Result.success(true)
        } catch (e: Exception) {
            prefsManager.clearSession()
            Result.failure(e)
        }
    }
}