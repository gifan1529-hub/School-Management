package com.example.schoolmanagement.Domain.Model

import kotlinx.serialization.Serializable

@Serializable
data class GradeSubjectItem(
    val subject: String,
    val score: String,
    val status: String,
    val grade: String
)