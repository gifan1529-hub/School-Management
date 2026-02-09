package com.example.schoolmanagement.Domain.Repository

import com.example.schoolmanagement.Domain.Model.LogData

interface ActivityLogRepository {
    suspend fun getActivityLogs(token: String, isAdmin: Boolean): Result<List<LogData>>
    suspend fun getUnreadCount(token: String): Result<Int>
    suspend fun markAllAsRead(token: String): Result<Boolean>
}