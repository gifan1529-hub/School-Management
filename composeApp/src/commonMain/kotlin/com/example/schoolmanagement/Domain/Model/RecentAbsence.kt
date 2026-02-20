package com.example.schoolmanagement.Domain.Model

import kotlinx.serialization.Serializable

@Serializable
data class RecentAbsence(
    val date_formatted: String,
    val status: String,
    val note: String
)