package com.example.schoolmanagement.Domain.Repository

import com.example.schoolmanagement.Data.Remote.AttendanceRecord
import com.example.schoolmanagement.Data.Remote.StudentAttendance

interface AttendanceRepository {
    suspend fun submitAttendance(qrCode: String, token: String, lat: Double, long: Double): Result<Boolean>
    suspend fun getHistoryAttendance(token: String): Result<List<AttendanceRecord>>
    suspend fun getClassAttendance(token: String, className: String): Result<List<StudentAttendance>>
    suspend fun getAttendanceStats(token: String): Result<List<AttendanceRecord>>
}
