package com.example.schoolmanagement.Domain.Model

import kotlinx.serialization.Serializable


@Serializable
data class UserMiniData(
    val id: Int,
    val name: String,
    val `class`: String? = null
)