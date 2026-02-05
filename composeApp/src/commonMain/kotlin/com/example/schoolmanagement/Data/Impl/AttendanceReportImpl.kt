package com.example.schoolmanagement.Data.Impl

import com.example.schoolmanagement.Data.Remote.ApiService
import com.example.schoolmanagement.Data.Remote.AttendanceReportResponse
import com.example.schoolmanagement.Domain.Repository.AttendanceReportRepository
import com.example.schoolmanagement.Utils.HandleException

class AttendanceReportImpl (
    private val apiService: ApiService
): AttendanceReportRepository {
    private val exceptionHandler = HandleException()

    override suspend fun getAttendanceReport(
        token: String,
        role: String,
        `class`: String,
        status: String
    ): Result<AttendanceReportResponse>{
        return try {
            val response = apiService.getAttendanceReport(token, role, `class`, status)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(exceptionHandler.handleException(e))
        }
    }
}