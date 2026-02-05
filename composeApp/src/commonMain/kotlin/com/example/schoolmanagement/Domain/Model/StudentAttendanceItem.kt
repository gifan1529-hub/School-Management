package com.example.schoolmanagement.Domain.Model

import kotlinx.serialization.Serializable

@Serializable
data class StudentAttendanceItem (
    val name: String,
    val status: String,
    val time_in: String,
    val `class`: String
)