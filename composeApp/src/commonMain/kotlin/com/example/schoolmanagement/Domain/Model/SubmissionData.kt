package com.example.schoolmanagement.Domain.Model

import kotlinx.serialization.Serializable

@Serializable
data class SubmissionData(
    val id: Int,
    val student_id: Int,
    val file: String,
    val created_at: String,
    val student: StudentData? = null
)