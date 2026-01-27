package com.example.schoolmanagement.Domain.Repository

interface AttendanceRepository {
    suspend fun submitAttendance(lat: Double, lon: Double): Result<Boolean>
}