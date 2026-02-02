package com.example.schoolmanagement.Domain.Repository

import com.example.schoolmanagement.Data.Remote.AttendanceRecord

interface AttendanceRepository {
    suspend fun submitAttendance(qrCode: String, token: String, lat: Double, long: Double): Result<Boolean>
    suspend fun getHistoryAttendance(token: String): Result<List<AttendanceRecord>>
}