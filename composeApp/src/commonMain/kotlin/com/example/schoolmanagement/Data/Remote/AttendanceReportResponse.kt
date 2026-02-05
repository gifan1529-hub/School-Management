package com.example.schoolmanagement.Data.Remote

import com.example.schoolmanagement.Domain.Model.ReportSummary
import com.example.schoolmanagement.Domain.Model.StudentAttendanceItem
import kotlinx.serialization.Serializable

@Serializable
data class AttendanceReportResponse (
    val message: String,
    val date: String,
    val summary: ReportSummary,
    val data: List<StudentAttendanceItem>
)