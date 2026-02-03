package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Data.Remote.ApiService
import com.example.schoolmanagement.Domain.Repository.AttendanceRepository
import com.example.schoolmanagement.getTodayDate
import kotlinx.coroutines.flow.first

class SubmitAttendanceUC(
    private val apiService: ApiService,
    private val repository: AttendanceRepository,
    private val prefsManager: PrefsManager
) {
    suspend fun invoke(qrCode: String, lat: Double, long: Double): Result<Boolean> {
        return try {
            val token = prefsManager.getAuthToken.first() ?: ""

            apiService.postAttendance(qrCode, token, lat, long)

            val today = getTodayDate()
            prefsManager.saveAbsenStatus(true, today)

            Result.success(true)
        } catch (e: Exception) {
            if (e.message?.contains("Kamu sudah absen hari ini", ignoreCase = true) == true) {
                val today = getTodayDate()
                prefsManager.saveAbsenStatus(true, today)
                return Result.success(true)
            }
            Result.failure(e)
        }
    }
}