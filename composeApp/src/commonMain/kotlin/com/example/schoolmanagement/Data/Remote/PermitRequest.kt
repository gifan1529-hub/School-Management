package com.example.schoolmanagement.Data.Remote

import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable

@Serializable
data class PermitRequest (
    val type: String,
    val timeIn: String,
    val timeOut: String,
    val reason: String,
    val start_date: String,
    val end_date: String
)
