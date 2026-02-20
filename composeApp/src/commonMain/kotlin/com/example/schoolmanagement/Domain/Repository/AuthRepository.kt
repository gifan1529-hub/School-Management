package com.example.schoolmanagement.Domain.Repository

interface AuthRepository {
    suspend fun updateFcmToken(token: String, fcmToken: String): Result<Boolean>
    suspend fun logout(): Result<Boolean>
}