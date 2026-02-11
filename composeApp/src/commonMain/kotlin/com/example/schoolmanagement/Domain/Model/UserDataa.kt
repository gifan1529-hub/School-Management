package com.example.schoolmanagement.Domain.Model

import kotlinx.serialization.Serializable

@Serializable
data class UserDataa (
    val id: Int,
    val name: String,
    val role: String,
)