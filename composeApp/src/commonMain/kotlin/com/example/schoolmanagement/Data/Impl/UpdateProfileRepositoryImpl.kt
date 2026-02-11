package com.example.schoolmanagement.Data.Impl

import com.example.schoolmanagement.Data.Remote.ApiService
import com.example.schoolmanagement.Data.Remote.UpdateProfileRequest
import com.example.schoolmanagement.Domain.Repository.UpdateProfileRepository
import com.example.schoolmanagement.Utils.HandleException

class UpdateProfileRepositoryImpl (
    private val apiService: ApiService
): UpdateProfileRepository {
    val exceptionHandler = HandleException()

    override suspend fun updateProfile(token: String, request: UpdateProfileRequest): Result<Boolean> {
        return try {
            apiService.updateProfile(token, request)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(exceptionHandler.handleException(e))
        }
    }
}