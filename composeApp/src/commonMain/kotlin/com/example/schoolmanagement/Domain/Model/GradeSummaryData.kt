package com.example.schoolmanagement.Domain.Model

import kotlinx.serialization.Serializable


@Serializable
data class GradeSummaryData(
    val semester_average: String,
    val subjects: List<GradeSubjectItem>
)