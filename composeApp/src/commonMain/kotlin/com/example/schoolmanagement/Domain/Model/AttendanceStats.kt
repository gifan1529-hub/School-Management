package com.example.schoolmanagement.Domain.Model

data class AttendanceStats(
    val hadir: String,
    val telat: String,
    val absen: String,
    val todayStatus: String,
    val isAlreadyAbsen: Boolean,
    val todayDate: String
)