package com.example.schoolmanagement.Data.Remote
import kotlinx.serialization.Serializable
@Serializable
data class ClassAttendanceResponse(
    val message: String,
    val data: List<StudentAttendance>
)

@Serializable
data class StudentAttendance(
    val id: Int,
    val name: String,
    val nisn: String?,
    val status: String,
    val time_in: String
)