package com.example.schoolmanagement.Domain.Repository

import com.example.schoolmanagement.Data.Remote.ScheduleItem

interface ScheduleRepository {
    suspend fun getAdminTeacherSchedule(token: String, teacherId: Int? = null): Result<List<ScheduleItem>>
    suspend fun getSchedules(token: String, `class`: String): Result<List<ScheduleItem>>
    suspend fun getTeacherSchedules(token: String): Result<List<ScheduleItem>>
}