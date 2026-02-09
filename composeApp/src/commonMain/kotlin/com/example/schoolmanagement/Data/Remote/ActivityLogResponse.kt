package com.example.schoolmanagement.Data.Remote

import com.example.schoolmanagement.Domain.Model.LogData
import kotlinx.serialization.Serializable

@Serializable
data class ActivityLogResponse(
    val data: List<LogData>
)