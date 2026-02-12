package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Data.Remote.ScheduleItem
import com.example.schoolmanagement.Domain.Repository.ScheduleRepository
import kotlinx.coroutines.flow.first

class GetAdminTeacherScheduleUseCase (
    private val repository: ScheduleRepository,
    private val prefsManager: PrefsManager
) {
    suspend operator fun invoke(teacherId: Int? = null): Result<List<ScheduleItem>> {
        return try {
        val token = prefsManager.getAuthToken.first() ?: ""
        if (token.isEmpty()) {
            return Result.failure(Exception("Sesi berakhir, silakan login ulang"))
        }
        return repository.getAdminTeacherSchedule(token, teacherId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}