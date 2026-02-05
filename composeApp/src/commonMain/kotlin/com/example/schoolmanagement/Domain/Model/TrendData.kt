package com.example.schoolmanagement.Domain.Model

import kotlinx.serialization.Serializable

@Serializable
data class TrendData (
    val label: String,
    val value: Float
)