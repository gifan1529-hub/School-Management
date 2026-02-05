package com.example.schoolmanagement.Data.Remote

import com.example.schoolmanagement.Domain.Model.SubmissionData
import kotlinx.serialization.Serializable

@Serializable
data class HomeWorkResponse (
    val id: Int,
    val teacher_id: Int,
    val subject: String,
    val `class`: String,
    val title: String,
    val description: String,
    val deadline: String,
    val status: String? = null,
    val file: String? = null,
    val created_at: String,
    val updated_at: String,
    val teacher: TeacherData? = null,
    val submissions: List<SubmissionData>? = emptyList()
)

@Serializable
data class TeacherData(
    val id: Int,
    val name: String
)