package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Data.Remote.ChildAttendanceResponse
import com.example.schoolmanagement.Domain.Repository.ParentAttendanceRepository
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Month
import kotlinx.datetime.number

class GetChildAttendanceUseCase (
    private val repository: ParentAttendanceRepository,
    private val prefsManager: PrefsManager
) {
    suspend operator fun invoke(month: Month, year: Int): Result<ChildAttendanceResponse> {
        val token = prefsManager.getAuthToken.first() ?: ""
        return repository.getChildAttendance(token, month.number, year)
    }
}