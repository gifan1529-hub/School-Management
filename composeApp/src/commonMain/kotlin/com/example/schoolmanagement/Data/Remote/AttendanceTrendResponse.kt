package com.example.schoolmanagement.Data.Remote

import com.example.schoolmanagement.Domain.Model.TrendData
import kotlinx.serialization.Serializable

@Serializable
data class AttendanceTrendResponse (
    val data: List<TrendData>
)