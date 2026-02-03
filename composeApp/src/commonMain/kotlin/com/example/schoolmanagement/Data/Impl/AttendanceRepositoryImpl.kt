package com.example.schoolmanagement.Data.Impl

import com.example.schoolmanagement.Data.Remote.ApiService
import com.example.schoolmanagement.Data.Remote.AttendanceRecord
import com.example.schoolmanagement.Data.Remote.StudentAttendance
import com.example.schoolmanagement.Domain.Model.AttendanceStats
import com.example.schoolmanagement.Domain.Repository.AttendanceRepository
import io.ktor.client.plugins.ResponseException
import kotlin.math.ln

class AttendanceRepositoryImpl(
    private val apiService: ApiService
) : AttendanceRepository {
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
            Result.failure(Exception(handleException(e)))
        }
    }

    override suspend fun getHistoryAttendance(token: String): Result<List<AttendanceRecord>> {
        return try {
            val response = apiService.getAttendanceHistory(token)
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(Exception(handleException(e)))
        }
    }

    override suspend fun getClassAttendance(token: String, className: String): Result<List<StudentAttendance>> {
        return try {
            val response = apiService.getClassAttendance(token, className)
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(Exception(handleException(e)))
        }
    }

    override suspend fun getAttendanceStats(token: String): Result<List<AttendanceRecord>> {
        return try {
            val response = apiService.getAttendanceHistory(token)
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(Exception(handleException(e)))
        }
    }
}