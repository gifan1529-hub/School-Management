package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Data.Remote.PermitRequest
import com.example.schoolmanagement.Domain.Repository.AttendanceRepository
import com.example.schoolmanagement.Domain.Repository.PermitRepository
import kotlinx.coroutines.flow.first

class SubmitPermitUseCase (
    private val repository: PermitRepository,
    private val prefsManager: PrefsManager
) {
    suspend fun invoke(
        type: String,
        startDate: String,
        endDate: String,
        reason: String,
        tIn: String,
        tOut: String
    ): Result<Boolean> {
        return try {
            val token = prefsManager.getAuthToken.first() ?: ""

            val request = PermitRequest(
                type = type,
                start_date = startDate,
                end_date = endDate,
                timeIn = tIn,
                timeOut = tOut,
                reason = reason,
            )

            val result = repository.submitPermit(token, request)
            if (result.isSuccess) {
                Result.success(true)
            } else {
                Result.failure(result.exceptionOrNull()!!)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}