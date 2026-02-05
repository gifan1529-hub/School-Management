package com.example.schoolmanagement.Domain.Repository

import com.example.schoolmanagement.Data.Remote.AttendanceReportResponse

interface AttendanceReportRepository {
    suspend fun getAttendanceReport(
        token: String,
        role: String,
        `class`: String,
        status: String
    ): Result<AttendanceReportResponse>
}