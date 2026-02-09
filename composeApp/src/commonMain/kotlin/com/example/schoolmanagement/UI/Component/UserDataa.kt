package com.example.schoolmanagement.UI.Component

import kotlinx.serialization.Serializable

@Serializable
data class UserDataa (
    val id: Int,
    val name: String,
    val role: String,
)