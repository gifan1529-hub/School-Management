package com.example.schoolmanagement.Domain.Repository

import com.example.schoolmanagement.Data.Remote.ScheduleItem

interface ScheduleRepository {

    suspend fun getSchedules(token: String, `class`: String): Result<List<ScheduleItem>>
}