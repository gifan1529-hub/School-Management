package com.example.schoolmanagement.Domain.Repository

import com.example.schoolmanagement.Domain.Model.AdminStatsData

interface StatsRepository {
    suspend fun getStats(token: String): Result<AdminStatsData>
}