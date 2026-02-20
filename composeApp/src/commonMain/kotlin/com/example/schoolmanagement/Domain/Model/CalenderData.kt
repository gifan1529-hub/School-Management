package com.example.schoolmanagement.Domain.Model

import kotlinx.serialization.Serializable

@Serializable
data class CalendarData(
    val date: String,
    val status: String,
    val color: String
)