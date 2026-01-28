package com.example.schoolmanagement.Data.Impl

import com.example.schoolmanagement.Data.Remote.ApiService
import com.example.schoolmanagement.Domain.Repository.AttendanceRepository
import kotlin.math.ln

class AttendanceRepositoryImpl(
    private val apiService: ApiService
) : AttendanceRepository {

    override suspend fun submitAttendance(qrCode: String, token: String): Result<Boolean> {
        return try {
            println("DEBUG API: Requesting API")
            val response = apiService.postAttendance(qrCode, token)
            println("DEBUG API: Response Message -> ${response.message}")
            if (response.message == "Absensi berhasil!") {
                Result.success(true)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            println("DEBUG API: CRASH di Repository -> ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }
}