package com.example.schoolmanagement.Domain.Model

import kotlinx.serialization.Serializable

@Serializable
data class ChatUserDetail (
    val id: Int = 0,
    val name: String = "Unknown",
    val role: String = "",
    val photo: String? = null
)