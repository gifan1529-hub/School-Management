package com.example.schoolmanagement.Domain.Model

import kotlinx.serialization.Serializable

@Serializable
data class UserDatas(
    val id: Int,
    val name: String,
    val email: String,
    val role: String,
    val nisn: String? = null,
    val `class`: String? = null,
    val created_at: String? = null
)