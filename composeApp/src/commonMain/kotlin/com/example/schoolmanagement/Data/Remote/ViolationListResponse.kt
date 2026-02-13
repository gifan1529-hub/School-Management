package com.example.schoolmanagement.Data.Remote

import com.example.schoolmanagement.Domain.Model.ViolationData
import kotlinx.serialization.Serializable

@Serializable
data class ViolationListResponse (
    val message: String,
    val data: List<ViolationData>
)