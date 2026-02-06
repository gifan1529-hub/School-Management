package com.example.schoolmanagement.Data.Remote

import com.example.schoolmanagement.Domain.Model.GradeSummaryData
import kotlinx.serialization.Serializable

@Serializable
data class GradeSummaryResponse(
    val message: String,
    val data: GradeSummaryData
)