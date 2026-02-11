package com.example.schoolmanagement.Data.Remote

import com.example.schoolmanagement.Domain.Model.PermitData
import kotlinx.serialization.Serializable

@Serializable
data class SubmitPermitResponse (
    val message: String,
    val data: PermitData
)