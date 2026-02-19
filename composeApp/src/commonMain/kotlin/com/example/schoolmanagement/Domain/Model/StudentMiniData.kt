package com.example.schoolmanagement.Domain.Model

import kotlinx.serialization.Serializable

@Serializable
data class StudentMiniData (
    val id: Int,
    val name: String,
    val `class`: String?,
    val nisn: String?
)