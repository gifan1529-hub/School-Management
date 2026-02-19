package com.example.schoolmanagement.Domain.Repository

import com.example.schoolmanagement.Data.Remote.ParentDashboardResponse
import com.example.schoolmanagement.Domain.Model.ParentDashboardData

interface ParentRepository {
    suspend fun getDashboard(token: String): Result<ParentDashboardData>
}