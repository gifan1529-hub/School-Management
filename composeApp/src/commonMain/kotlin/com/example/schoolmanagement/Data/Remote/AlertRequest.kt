package com.example.schoolmanagement.Data.Remote

import kotlinx.serialization.Serializable

@Serializable
data class AlertRequest(
    val title: String,
    val message: String,
    val audience: String, // isinya: "all", "teacher", atau "student"
    val type: String // isinya: "info", "warning", atau "urgent"
)