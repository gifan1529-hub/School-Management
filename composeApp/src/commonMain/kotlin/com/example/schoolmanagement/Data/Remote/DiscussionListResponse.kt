package com.example.schoolmanagement.Data.Remote

import com.example.schoolmanagement.Domain.Model.DiscussionData
import kotlinx.serialization.Serializable

@Serializable
data class DiscussionListResponse (
    val message: String,
    val data: List<DiscussionData> = emptyList()
)