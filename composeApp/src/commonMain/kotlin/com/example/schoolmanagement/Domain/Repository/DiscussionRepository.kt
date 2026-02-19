package com.example.schoolmanagement.Domain.Repository

import com.example.schoolmanagement.Domain.Model.DiscussionData
import kotlinx.coroutines.flow.Flow

interface DiscussionRepository {
    fun observeDiscussions(homeworkId: Int): Flow<List<DiscussionData>>
    suspend fun sendDiscussions(homeworkId: Int, message: String): Result<Boolean>
    suspend fun getDiscussions(token: String, homeworkId: Int): Result<List<DiscussionData>>
    suspend fun sendDiscussion(token: String, homeworkId: Int, message: String): Result<Boolean>
}