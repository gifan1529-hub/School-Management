package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Data.Remote.AttendanceRecord
import com.example.schoolmanagement.Domain.Repository.AttendanceRepository
import kotlinx.coroutines.flow.first

class GetAttendanceHistoryUseCase (
    private val repository: AttendanceRepository,
    private val prefsManager: PrefsManager
) {
    suspend fun invoke(): Result<List<AttendanceRecord>> {
        val token = prefsManager.getAuthToken.first() ?: ""
        return repository.getHistoryAttendance(token)
    }
}