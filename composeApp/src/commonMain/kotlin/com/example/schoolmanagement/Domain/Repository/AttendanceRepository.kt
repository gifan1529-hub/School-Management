package com.example.schoolmanagement.Domain.Repository

interface AttendanceRepository {
    suspend fun submitAttendance(qrCode: String, token: String): Result<Boolean>
}