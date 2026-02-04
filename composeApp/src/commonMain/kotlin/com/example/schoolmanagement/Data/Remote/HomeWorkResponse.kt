package com.example.schoolmanagement.Data.Remote

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
    val file: String? = null,
    val created_at: String,
    val updated_at: String,
    val teacher: TeacherData? = null
)

@Serializable
data class TeacherData(
    val id: Int,
    val name: String
)