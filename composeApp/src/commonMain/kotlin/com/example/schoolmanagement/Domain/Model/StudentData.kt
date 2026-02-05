package com.example.schoolmanagement.Domain.Model

import kotlinx.serialization.Serializable


@Serializable
data class StudentData(
    val id: Int,
    val name: String
)