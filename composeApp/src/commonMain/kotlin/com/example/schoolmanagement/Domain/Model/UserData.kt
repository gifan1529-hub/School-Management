package com.example.schoolmanagement.Domain.Model

import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val name: String,
    val role: String,
    val `class`: String? = null
)