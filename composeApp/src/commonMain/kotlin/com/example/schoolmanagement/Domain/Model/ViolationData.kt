package com.example.schoolmanagement.Domain.Model

import kotlinx.serialization.Serializable

@Serializable
data class ViolationData (
    val id: Int,
    val student_id: Int,
    val teacher_id: Int,
    val violation_name: String,
    val category: String,
    val points: Int,
    val description: String? = null,
    val image: String? = null,
    val created_at: String,
    val student: UserMiniData? = null,
    val teacher: UserMiniData? = null
)