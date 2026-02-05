package com.example.schoolmanagement.Data.Remote

import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable

@Serializable
data class HomeWorkDetailResponse (
    val data: HomeWorkResponse
)