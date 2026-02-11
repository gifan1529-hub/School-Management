package com.example.schoolmanagement.Domain.Repository

import com.example.schoolmanagement.Domain.Model.GradeDetailData
import com.example.schoolmanagement.Domain.Model.GradeSummaryData

interface GradeRepository {
    suspend fun getGradeSummary(token: String): Result<GradeSummaryData>
    suspend fun getMyGrades(token: String, subject: String? = null): Result<List<GradeDetailData>>
}