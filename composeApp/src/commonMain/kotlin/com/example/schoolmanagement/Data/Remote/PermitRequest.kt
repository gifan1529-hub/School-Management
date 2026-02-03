package com.example.schoolmanagement.Data.Remote

import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable

@Serializable
data class PermitRequest (
    val type: String,
    val date: String,
    val timeIn: String,
    val timeOut: String,
    val reason: String,
)
