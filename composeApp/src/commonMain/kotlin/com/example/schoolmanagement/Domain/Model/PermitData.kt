package com.example.schoolmanagement.Domain.Model

import kotlinx.serialization.Serializable

@Serializable
data class PermitData(
    val id: Int,
    val type: String,
    val start_date: String,
    val end_date: String,
    val timeIn: String,
    val timeOut: String,
    val reason: String? = null,
    val status: String,
    val user: UserData? = null,
    val image: String? = null
)