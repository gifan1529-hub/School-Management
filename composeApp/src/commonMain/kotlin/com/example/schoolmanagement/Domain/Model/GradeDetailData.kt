package com.example.schoolmanagement.Domain.Model

import kotlinx.serialization.Serializable

@Serializable
data class GradeDetailData(
    val id: Int,
    val title: String,
    val subject: String,
    val teacher: String,
    val submitted_at: String,
    val file_url: String,
    val grade: String,
    val status: String
)