package com.example.schoolmanagement.Data.Remote

import com.example.schoolmanagement.Domain.Model.GradeDetailData
import kotlinx.serialization.Serializable

@Serializable
data class MyGradeResponse(
    val message: String = "",
    val data: List<GradeDetailData> = emptyList()
)