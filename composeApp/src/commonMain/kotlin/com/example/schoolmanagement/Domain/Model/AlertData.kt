package com.example.schoolmanagement.Domain.Model

import kotlinx.serialization.Serializable

@Serializable
data class AlertData(
    val title: String,
    val message: String,
    val audience: String,
    val type: String,
    val created_at: String,
    val creator: CreatorData? = null
)

@Serializable
data class CreatorData(
    val id: Int,
    val name: String
)