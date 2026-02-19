package com.example.schoolmanagement.Data.Impl

import com.example.schoolmanagement.Data.Remote.ApiService
import com.example.schoolmanagement.Domain.Model.ParentDashboardData
import com.example.schoolmanagement.Domain.Repository.ParentRepository
import com.example.schoolmanagement.Utils.HandleException

class ParentRepositoryImpl (
    private val apiService: ApiService
): ParentRepository {
    private val exceptionHandler = HandleException()

    override suspend fun getDashboard(token: String): Result<ParentDashboardData> {
        return try {
            val response = apiService.getParentDashboard(token)
            response.data?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Data tidak ditemukan"))
        } catch (e: Exception) {
            Result.failure(exceptionHandler.handleException(e))
        }
    }
}