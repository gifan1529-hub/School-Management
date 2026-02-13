package com.example.schoolmanagement.Domain.Repository

import com.example.schoolmanagement.Domain.Model.DiscussionData

interface DiscussionRepository {
    suspend fun getDiscussions(token: String, homeworkId: Int): Result<List<DiscussionData>>
    suspend fun sendDiscussion(token: String, homeworkId: Int, message: String): Result<Boolean>
}