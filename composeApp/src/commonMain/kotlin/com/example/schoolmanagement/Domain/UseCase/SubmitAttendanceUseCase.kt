package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Domain.Repository.AttendanceRepository
import com.example.schoolmanagement.getTodayDate

class SubmitAttendanceUC(
    private val repository: AttendanceRepository,
    private val prefsManager: PrefsManager
) {
    suspend fun invoke(qrCode: String, token: String): Result<Boolean> {
        return try {
            val result = repository.submitAttendance(qrCode , token)
            if (result.isSuccess) {
                val today = getTodayDate()
                // Simpan status ke Prefs agar saat dibuka lagi tetap 'true'
                prefsManager.saveAbsenStatus(true, date = today)
                Result.success(true)
            } else {
                Result.failure(Exception("Gagal Absen"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}