package com.example.schoolmanagement.Data.Remote

import kotlinx.serialization.Serializable

@Serializable
data class HomeWorkListResponse  (
    val message: String,
    val data: List<HomeWorkResponse>
)