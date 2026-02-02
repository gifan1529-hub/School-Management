package com.example.schoolmanagement.Data.Remote

import kotlinx.serialization.Serializable

@Serializable
data class AttendanceRequest (
    val qr_code: String,
    val lat: Double,
    val long: Double
)
