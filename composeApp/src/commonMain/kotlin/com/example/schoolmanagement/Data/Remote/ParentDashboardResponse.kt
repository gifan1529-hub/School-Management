package com.example.schoolmanagement.Data.Remote

import com.example.schoolmanagement.Domain.Model.ParentDashboardData
import kotlinx.serialization.Serializable

@Serializable
data class ParentDashboardResponse (
    val message: String,
    val data: ParentDashboardData? = null
)