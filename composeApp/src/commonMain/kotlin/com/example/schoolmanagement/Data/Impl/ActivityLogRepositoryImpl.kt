package com.example.schoolmanagement.Data.Impl

import com.example.schoolmanagement.Data.Remote.ActivityLogResponse
import com.example.schoolmanagement.Data.Remote.ApiService
import com.example.schoolmanagement.Domain.Model.LogData
import com.example.schoolmanagement.Domain.Repository.ActivityLogRepository
import com.example.schoolmanagement.Utils.HandleException

class ActivityLogRepositoryImpl (
    private val apiService: ApiService
): ActivityLogRepository {
    private val exceptionHandler = HandleException()

    override suspend fun getActivityLogs(token: String, isAdmin: Boolean): Result<List<LogData>> {
        return try {
            val response = if (isAdmin) {
                apiService.getAllActivities(token)
            } else {
                apiService.getMyActivities(token)
            }
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(exceptionHandler.handleException(e))
        }
    }

    override suspend fun getUnreadCount(token: String): Result<Int> {
        return try {
            val count = apiService.getUnreadActivity(token)
            Result.success(count)
        } catch (e: Exception) {
            Result.failure(exceptionHandler.handleException(e))
        }
    }

    override suspend fun markAllAsRead(token: String): Result<Boolean> {
        return try {
            apiService.markAllActivitiesAsRead(token)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(exceptionHandler.handleException(e))
        }
    }
}