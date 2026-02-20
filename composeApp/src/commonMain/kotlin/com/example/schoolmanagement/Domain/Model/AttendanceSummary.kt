package com.example.schoolmanagement.Domain.Model

import kotlinx.serialization.Serializable


@Serializable
data class AttendanceSummary(
    val percentage: String,
    val present_count: Int,
    val total_school_days: Int,
    val missed_count: Int
)