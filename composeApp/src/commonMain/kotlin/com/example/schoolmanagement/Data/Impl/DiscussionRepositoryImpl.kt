package com.example.schoolmanagement.Data.Impl

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Data.Remote.ApiService
import com.example.schoolmanagement.Domain.Model.DiscussionData
import com.example.schoolmanagement.Domain.Model.UserMiniData
import com.example.schoolmanagement.Domain.Repository.DiscussionRepository
import com.example.schoolmanagement.Utils.HandleException
import dev.gitlive.firebase.database.database
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.collections.emptyList

class DiscussionRepositoryImpl (
    private val apiService: ApiService,
    private val prefs: PrefsManager
): DiscussionRepository {
    private val db = dev.gitlive.firebase.Firebase.database(
        url = "https://schooldata-bd691-default-rtdb.asia-southeast1.firebasedatabase.app"
    ).reference("discussions")

    val exceptionHandler = HandleException()

    override fun observeDiscussions(homeworkId: Int): Flow<List<DiscussionData>> =
        db.child(homeworkId.toString())
        .valueEvents
        .map { snapshot ->
            try {
                val chatMap = snapshot.value<Map<String, DiscussionData>?>()
                chatMap?.values?.sortedBy { it.created_at } ?: emptyList()
            } catch (e: Exception) {
                println("DEBUG FIREBASE ERROR: ${e.message}")
                e.printStackTrace()
                emptyList()
            }
        }

    override suspend fun sendDiscussions(
        homeworkId: Int,
        message: String
    ): Result<Boolean> {
        return try {
            val token = prefs.getAuthToken.first() ?: ""
            val result = sendDiscussion(token, homeworkId, message)
            result
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

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