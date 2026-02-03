package com.example.schoolmanagement.Data.Remote

import com.example.schoolmanagement.Domain.Model.AlertData
import kotlinx.serialization.Serializable

@Serializable
data class AlertResponse(
    val announcement: AlertData,
)

@Serializable
data class AlertListResponse(
    val message: String,
    val data: List<AlertData>
)