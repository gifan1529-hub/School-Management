package com.example.schoolmanagement.Data.Impl

import com.example.schoolmanagement.Data.Remote.ApiService
import com.example.schoolmanagement.Domain.Model.DiscussionData
import com.example.schoolmanagement.Domain.Repository.DiscussionRepository
import com.example.schoolmanagement.Utils.HandleException

class DiscussionRepositoryImpl (
    private val apiService: ApiService
): DiscussionRepository {

    val exceptionHandler = HandleException()

    override suspend fun getDiscussions(
        token: String,
        homeworkId: Int
    ): Result<List<DiscussionData>> {
        return try {
            val response = apiService.getDiscussions(token, homeworkId)
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(exceptionHandler.handleException(e))
        }
    }

    override suspend fun sendDiscussion(
        token: String,
        homeworkId: Int,
        message: String
    ): Result<Boolean> {
        return try {
            apiService.postDiscussion(token, homeworkId, message)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(exceptionHandler.handleException(e))
        }
    }
}