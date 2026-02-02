package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Data.Remote.ApiService
import com.example.schoolmanagement.Domain.Model.AttendanceStats
import com.example.schoolmanagement.getTodayDateS
import kotlinx.coroutines.flow.first

import com.example.schoolmanagement.Domain.Repository.AttendanceRepository

class GetAttendanceStatusUseCase (
    private val apiService: ApiService,
    private val repository: AttendanceRepository,
    private val prefsManager: PrefsManager
) {
    suspend operator fun invoke(): Result<AttendanceStats> {
        return try {
            val token = prefsManager.getAuthToken.first() ?: ""
            if (token.isNotEmpty()) {
                val response = apiService.getAttendanceHistory(token)
                val history = response.data
                val todayDate = getTodayDateS()

                val hadir = history.count { it.status == "Present" }
                val telat = history.count { it.status == "Late" }
                val absen = history.count { it.status == "Absent" }
                val todayRecord = history.find { it.date == todayDate }
                val hasRecordToday = todayRecord != null

                // Cek apakah ada absen hari ini
                val hasAttendedToday = response.data.any { it.date == todayDate }

                Result.success(
                    AttendanceStats(
                        hadir = hadir.toString(),
                        telat = telat.toString(),
                        absen = absen.toString(),
                        todayStatus = todayRecord?.status ?: "",
                        isAlreadyAbsen = hasAttendedToday,
                        todayDate = todayDate
                    )
                )
            } else {
                Result.failure(Exception("Token empty"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}