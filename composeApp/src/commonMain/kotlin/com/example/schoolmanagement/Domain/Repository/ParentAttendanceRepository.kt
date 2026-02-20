package com.example.schoolmanagement.Domain.Repository

import com.example.schoolmanagement.Data.Remote.ChildAttendanceResponse

interface ParentAttendanceRepository {
    suspend fun getChildAttendance(token: String, month: Int, year: Int): Result<ChildAttendanceResponse>
}