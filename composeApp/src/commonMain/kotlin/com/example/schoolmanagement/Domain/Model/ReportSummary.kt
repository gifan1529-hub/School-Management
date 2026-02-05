package com.example.schoolmanagement.Domain.Model

import kotlinx.serialization.Serializable

@Serializable
data class ReportSummary(
    val total: Int,
    val hadir: Int,
    val alpa: Int
)