package com.example.schoolmanagement.Domain.Model

import com.example.schoolmanagement.Data.Remote.TeacherData
import kotlinx.serialization.Serializable

@Serializable
data class MaterialData(
    val id: Int,
    val title: String,
    val description: String? = null,
    val subject: String,
    val `class`: String,
    val type: String, // file, link, video
    val content: String, // URL file atau URL Link
    val teacher_id: Int,
    val created_at: String,
    val teacher: TeacherData? = null
)