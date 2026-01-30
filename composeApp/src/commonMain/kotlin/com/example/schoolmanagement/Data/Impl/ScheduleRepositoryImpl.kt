package com.example.schoolmanagement.Data.Impl

import com.example.schoolmanagement.Data.Remote.ApiService
import com.example.schoolmanagement.Domain.Repository.ScheduleRepository
import com.example.schoolmanagement.Data.Remote.ScheduleItem

class ScheduleRepositoryImpl (
    private val apiService: ApiService
): ScheduleRepository {
    override suspend fun getSchedules(token: String, `class`: String): Result<List<ScheduleItem>> {
        return try {
            val response = apiService.getSchedules(token, `class`)
            println("DEBUG API: $response")
            if (response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception("Gagal mengambil jadwal"))
            }
        } catch (e: Exception) {
            println("DEBUG API: ${e.message}")
            Result.failure(e)
        }
    }
}