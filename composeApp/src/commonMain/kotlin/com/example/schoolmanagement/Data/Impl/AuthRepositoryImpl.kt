package com.example.schoolmanagement.Data.Impl

import com.example.schoolmanagement.Data.Remote.ApiService
import com.example.schoolmanagement.Domain.Repository.AuthRepository
import com.example.schoolmanagement.Utils.HandleException

class AuthRepositoryImpl (
    private val apiService: ApiService
): AuthRepository {
    private val exceptionHandler = HandleException()

    override suspend fun updateFcmToken(token: String, fcmToken: String): Result<Boolean> {return try {
        apiService.sendFcmToken(token, fcmToken)
        Result.success(true)
    } catch (e: Exception) {
        Result.failure(exceptionHandler.handleException(e))
    }
    }
}