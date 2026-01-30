package com.example.schoolmanagement.Data.Remote

import kotlinx.serialization.Serializable

@Serializable
data class AttendanceHistoryResponse (
    val message: String,
    val data: List<AttendanceRecord>
)

@Serializable
data class AttendanceRecord (
    val id: Int,
    val date: String,
    val status: String,
    val time_in: String
)
