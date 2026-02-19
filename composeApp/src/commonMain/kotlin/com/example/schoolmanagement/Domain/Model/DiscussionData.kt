package com.example.schoolmanagement.Domain.Model

import kotlinx.serialization.Serializable

@Serializable
data class DiscussionData(
    val id: Int,
    val homework_id: Int = 0,
    val user_id: Int = 0,
    val name: String = "",
    val role: String = "",
    val message: String = "",
    val created_at: String = "",
)