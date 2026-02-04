package com.example.schoolmanagement.Data.Remote

import kotlinx.serialization.Serializable

@Serializable
data class HomeWorkRequest(
    val subject: String,
    val `class`: String,
    val title: String,
    val description: String,
    val deadline: String
)