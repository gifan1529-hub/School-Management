package com.example.schoolmanagement.Domain.Model

import kotlinx.serialization.Serializable

@Serializable
data class PermitData(
    val id: Int,
    val status: String
)