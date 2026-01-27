package com.example.schoolmanagement.Data.Impl

import com.example.schoolmanagement.Data.Remote.ApiService
import com.example.schoolmanagement.Domain.Repository.AttendanceRepository

class AttendanceRepositoryImpl(
    private val apiService: ApiService
) : AttendanceRepository {

    override suspend fun submitAttendance(lat: Double, lon: Double): Result<Boolean> {
        return try {
            val response = apiService.postAttendance(lat, lon)

            if (response.message == "Absensi berhasil!") {
                Result.success(true)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}