package com.example.schoolmanagement.Data.Impl

import com.example.schoolmanagement.Data.Remote.ApiService
import com.example.schoolmanagement.Data.Remote.AttendanceRecord
import com.example.schoolmanagement.Domain.Repository.AttendanceRepository
import kotlin.math.ln

class AttendanceRepositoryImpl(
    private val apiService: ApiService
) : AttendanceRepository {

    override suspend fun submitAttendance(qrCode: String, token: String, lat: Double, long: Double): Result<Boolean> {
        return try {
            println("DEBUG API: Requesting API")
            val response = apiService.postAttendance(qrCode, token, lat, long)
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

    override suspend fun getHistoryAttendance(token: String): Result<List<AttendanceRecord>> {
        return try {
            val response = apiService.getAttendanceHistory(token)
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}