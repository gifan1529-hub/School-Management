package com.example.schoolmanagement.Domain.Model

import kotlinx.serialization.Serializable

@Serializable
data class ParentDashboardData(
    val child_name: String,
    val `class`: String,
    val total_attendance: Int,
    val avg_marks: Float? = null,
    val nisn: String,
    val total_violations: Int,
    val student: StudentMiniData? = null
)