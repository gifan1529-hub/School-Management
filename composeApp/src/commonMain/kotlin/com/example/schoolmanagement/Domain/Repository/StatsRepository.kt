package com.example.schoolmanagement.Domain.Repository

import com.example.schoolmanagement.Domain.Model.AdminStatsData
import com.example.schoolmanagement.Domain.Model.TrendData

interface StatsRepository {
    suspend fun getStats(token: String): Result<AdminStatsData>
    suspend fun getAttendanceTrend(token: String): Result<List<TrendData>>
}


