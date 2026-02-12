package com.example.schoolmanagement.Data.Impl

import com.example.schoolmanagement.Data.Remote.ApiService
import com.example.schoolmanagement.Domain.Repository.ScheduleRepository
import com.example.schoolmanagement.Data.Remote.ScheduleItem
import io.ktor.client.plugins.ResponseException

class ScheduleRepositoryImpl (
    private val apiService: ApiService
): ScheduleRepository {
    private fun handleException(e: Exception): Exception {
        return if (e is ResponseException) {
            val status = e.response.status.value
            val message = when (status) {
                400 -> "Permintaan tidak valid (400)"
                401 -> "Sesi berakhir, silakan login ulang (401)"
                403 -> "Anda tidak memiliki akses (403)"
                404 -> "Data tidak ditemukan (404)"
                500 -> "Server sedang bermasalah (500)"
                else -> "Terjadi kesalahan server: $status"
            }
            Exception(message)
        } else {
            Exception("Masalah koneksi: ${e.message}")
        }
    }

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
            Result.failure(Exception(handleException(e)))
        }
    }

    override suspend fun getTeacherSchedules(token: String): Result<List<ScheduleItem>> {
        return try {
            val response = apiService.getMySchedules(token)
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(handleException(e))
        }
    }

    override suspend fun getAdminTeacherSchedule(token: String, teacherId: Int?): Result<List<ScheduleItem>> {
        return try {
            val response = apiService.getAdminTeacherSchedule(token, teacherId)

            if (response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception("Jadwal mengajar tidak ditemukan"))
            }
        } catch (e: Exception) {
            Result.failure(handleException(e))
        }
    }
}