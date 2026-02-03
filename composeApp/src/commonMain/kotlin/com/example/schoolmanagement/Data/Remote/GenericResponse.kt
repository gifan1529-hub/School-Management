package com.example.schoolmanagement.Data.Remote

import kotlinx.serialization.Serializable

@Serializable
data class GenericResponse(
    val message: String
)