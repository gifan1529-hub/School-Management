package com.example.schoolmanagement.Data.Remote

import kotlinx.serialization.Serializable


@Serializable
data class UpdateProfileResponse(
    val message: String,
    val user: UserApiResponse
)