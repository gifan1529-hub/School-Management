package com.example.schoolmanagement.Data.Remote

import kotlinx.serialization.Serializable

@Serializable
data class ScheduleResponse(
    val message: String = "",
    val data: List<ScheduleItem> = emptyList()
)

@Serializable
data class ScheduleItem (
    val id: Int? = null,
    val subject: String = "",
    val `class`: String = "",
    val day: String = "",
    val time_in: String = "",
    val time_out: String = "",
    val teacher_id: String = "",
    val teacher: Teacher? = null,
)

@Serializable
data class Teacher (
    val teacher_id: Int? = null,
    val name: String = ""
)

