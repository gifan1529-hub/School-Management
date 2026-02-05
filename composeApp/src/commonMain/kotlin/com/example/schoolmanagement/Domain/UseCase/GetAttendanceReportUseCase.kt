package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Data.Remote.AttendanceReportResponse
import com.example.schoolmanagement.Domain.Repository.AttendanceReportRepository
import kotlinx.coroutines.flow.first

class GetAttendanceReportUseCase(
    private val repository: AttendanceReportRepository,
    private val prefsManager: PrefsManager
) {
    suspend operator fun invoke(
        role: String,
        className: String,
        status: String
    ): Result<AttendanceReportResponse>{
        val token = prefsManager.getAuthToken.first() ?: ""
        return repository.getAttendanceReport(token, role, className, status)
    }
}