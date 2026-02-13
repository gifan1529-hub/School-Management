package com.example.schoolmanagement.Domain.Model

import kotlinx.serialization.Serializable

@Serializable
data class DiscussionData(
    val id: Int,
    val homework_id: Int,
    val user_id: Int,
    val message: String,
    val created_at: String,
    val user: UserMiniData
)