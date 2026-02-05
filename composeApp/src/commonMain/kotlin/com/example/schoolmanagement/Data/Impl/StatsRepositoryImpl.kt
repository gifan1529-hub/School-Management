package com.example.schoolmanagement.Data.Impl

import com.example.schoolmanagement.Data.Remote.ApiService
import com.example.schoolmanagement.Domain.Model.AdminStatsData
import com.example.schoolmanagement.Domain.Repository.StatsRepository
import com.example.schoolmanagement.Utils.HandleException

class StatsRepositoryImpl(
    private val apiService: ApiService
): StatsRepository {
    private val exceptionHandler = HandleException()

    override suspend fun getStats(token: String): Result<AdminStatsData> {
        return try {
            val response = apiService.getStats(token)
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(exceptionHandler.handleException(e))
        }
    }
}