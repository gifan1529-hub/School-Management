package com.example.schoolmanagement.Data.Remote

import com.example.schoolmanagement.Domain.Model.MaterialData
import kotlinx.serialization.Serializable

@Serializable
data class MaterialListResponse(
    val message: String,
    val data: List<MaterialData> = emptyList()
)