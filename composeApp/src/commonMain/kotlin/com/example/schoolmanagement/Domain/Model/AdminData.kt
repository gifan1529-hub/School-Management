package com.example.schoolmanagement.Domain.Model

import kotlinx.serialization.Serializable

@Serializable
data class AdminStatsData(
    val total_siswa: String,
    val total_guru: String,
    val total_kelas: String
)