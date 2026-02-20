package com.example.schoolmanagement.Data.Impl

import com.example.schoolmanagement.Data.Remote.ApiService
import com.example.schoolmanagement.Data.Remote.ChildAttendanceResponse
import com.example.schoolmanagement.Domain.Repository.ParentAttendanceRepository
import com.example.schoolmanagement.Utils.HandleException

class ParentAttendanceRepositoryImpl (
    private val apiService: ApiService
) : ParentAttendanceRepository {
    private val exceptionHandler = HandleException()

    override suspend fun getChildAttendance(
        token: String,
        month: Int,
        year: Int
    ): Result<ChildAttendanceResponse> {
        return try {
            val response = apiService.getChildAttendance(token, month, year)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(exceptionHandler.handleException(e))
        }
    }

}
