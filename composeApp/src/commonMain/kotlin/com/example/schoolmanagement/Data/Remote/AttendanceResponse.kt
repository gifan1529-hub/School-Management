package com.example.schoolmanagement.Data.Remote

import kotlinx.serialization.Serializable

@Serializable
data class AttendanceResponse (
    val message: String,
    val attendance: AttendanceDetail? = null
)
@Serializable
data class AttendanceDetail(
    val id: Int? = null,
    val user_id: Int? = null,
    val date: String? = null,
    val time_in: String? = null,
    val status: String? = null,
    val note: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null
)

