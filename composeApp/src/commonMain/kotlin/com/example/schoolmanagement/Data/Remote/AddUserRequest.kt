package com.example.schoolmanagement.Data.Remote

import kotlinx.serialization.Serializable

@Serializable
data class AddUserRequest(
    val name: String,
    val email: String,
    val password: String,
    val nisn: String,
    val role: String,
    val`class`: String? = null
)