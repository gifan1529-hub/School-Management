package com.example.schoolmanagement.Data.Remote

import com.example.schoolmanagement.Domain.Model.AttendanceSummary
import com.example.schoolmanagement.Domain.Model.CalendarData
import com.example.schoolmanagement.Domain.Model.RecentAbsence
import kotlinx.serialization.Serializable

@Serializable
data class ChildAttendanceResponse (
    val message: String? = "",
    val summary: AttendanceSummary,
    val calendar: List<CalendarData>,
    val recent_absences: List<RecentAbsence>
)