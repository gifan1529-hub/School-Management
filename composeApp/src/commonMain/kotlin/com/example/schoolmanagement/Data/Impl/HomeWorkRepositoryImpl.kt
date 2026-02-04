package com.example.schoolmanagement.Data.Impl

import com.example.schoolmanagement.Data.Remote.ApiService
import com.example.schoolmanagement.Data.Remote.HomeWorkRequest
import com.example.schoolmanagement.Data.Remote.HomeWorkResponse
import com.example.schoolmanagement.Domain.Repository.HomeWorkRepository
import com.example.schoolmanagement.Utils.HandleException

class HomeWorkRepositoryImpl (
    private val apiService: ApiService
): HomeWorkRepository {
    private val exceptionHandler = HandleException()

    override suspend fun getHomeworks(token: String): Result<List<HomeWorkResponse>> {
        return try {
            val response = apiService.getHomeworks(token)
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(exceptionHandler.handleException(e))
        }
    }

    override suspend fun postHomework(token: String, request: HomeWorkRequest): Result<Boolean> {
        return try {
            apiService.postHomework(token, request)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(exceptionHandler.handleException(e))
        }
    }

    override suspend fun deleteHomework(token: String, id: Int): Result<Boolean> {
        return try {
            apiService.deleteHomework(token, id)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(exceptionHandler.handleException(e))
        }
    }
}