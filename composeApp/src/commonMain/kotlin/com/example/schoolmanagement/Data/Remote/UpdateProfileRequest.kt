package com.example.schoolmanagement.Data.Remote

import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequest (
    val name: String? = null,
    val nis: String? = null,
    val nisn: String? = null,
    val phone: String? = null,
    val gender: String? = null,
    val `class`: String? = null,
    val address: String? = null,
    val photo: String? = null
)