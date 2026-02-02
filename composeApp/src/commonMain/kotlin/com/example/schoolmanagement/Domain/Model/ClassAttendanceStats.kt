package com.example.schoolmanagement.Domain.Model

import com.example.schoolmanagement.Data.Remote.StudentAttendance

data class ClassAttendanceStats (
    val hadir: String,
    val telat: String,
    val absen: String,
    val studentList: List<StudentAttendance>
)