package com.example.schoolmanagement.Data.Remote

import com.example.schoolmanagement.Domain.Model.AdminStatsData
import kotlinx.serialization.Serializable

@Serializable
data class AdminStatsResponse(
    val message: String,
    val data: AdminStatsData
)

