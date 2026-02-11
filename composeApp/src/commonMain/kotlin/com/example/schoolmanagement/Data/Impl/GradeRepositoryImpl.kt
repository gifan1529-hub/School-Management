package com.example.schoolmanagement.Data.Impl

import com.example.schoolmanagement.Data.Remote.ApiService
import com.example.schoolmanagement.Domain.Model.GradeDetailData
import com.example.schoolmanagement.Domain.Model.GradeSummaryData
import com.example.schoolmanagement.Domain.Repository.GradeRepository
import com.example.schoolmanagement.Utils.HandleException

class GradeRepositoryImpl (
    private val apiService: ApiService
) : GradeRepository {
    private val exceptionHandler = HandleException()

    override suspend fun getGradeSummary(token: String): Result<GradeSummaryData> {
        return try {
            val response = apiService.getGradeSummary(token)
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(exceptionHandler.handleException(e))
        }
    }

    override suspend fun getMyGrades(token: String, subject: String?): Result<List<GradeDetailData>> {
        return try {
            val response = apiService.getMyGrades(token, subject)
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(exceptionHandler.handleException(e))
        }
    }

}