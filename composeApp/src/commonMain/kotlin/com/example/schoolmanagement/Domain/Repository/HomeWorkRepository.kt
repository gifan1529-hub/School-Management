package com.example.schoolmanagement.Domain.Repository

import com.example.schoolmanagement.Data.Remote.HomeWorkRequest
import com.example.schoolmanagement.Data.Remote.HomeWorkResponse

interface  HomeWorkRepository {
    suspend fun getHomeworks(token: String): Result<List<HomeWorkResponse>>
    suspend fun postHomework(token: String, request: HomeWorkRequest): Result<Boolean>
    suspend fun deleteHomework(token: String, id: Int): Result<Boolean>
    suspend fun submitHomework(
        token: String,
        homeworkId: Int,
        fileBytes: ByteArray,
        fileName: String
    ): Result<Boolean>
    suspend fun getHomeworkDetail(token: String, id: Int): Result<HomeWorkResponse>
    suspend fun giveGrade(token: String, submissionId: Int, grade: Int): Result<Boolean>
}