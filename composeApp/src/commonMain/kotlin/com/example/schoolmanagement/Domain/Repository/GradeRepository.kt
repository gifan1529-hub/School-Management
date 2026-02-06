package com.example.schoolmanagement.Domain.Repository

import com.example.schoolmanagement.Domain.Model.GradeSummaryData

interface GradeRepository {
    suspend fun getGradeSummary(token: String): Result<GradeSummaryData>
}