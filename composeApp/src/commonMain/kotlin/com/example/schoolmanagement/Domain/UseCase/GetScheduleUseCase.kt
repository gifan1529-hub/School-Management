package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Data.Remote.ScheduleItem
import com.example.schoolmanagement.Domain.Repository.ScheduleRepository
import kotlinx.coroutines.flow.first

class GetScheduleUseCase (
    private val repository: ScheduleRepository,
    private val prefs: PrefsManager
) {
    suspend fun invoke(): Result<List<ScheduleItem>> {
        return try {
            val token = prefs.getAuthToken.first() ?: ""
            println("DEBUG TOKEN: $token")
            val userClass = prefs.getClass.first() ?: ""

            // manggil repository  filter kelas yang sudah diambil otomatis
            repository.getSchedules(token, userClass)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}