package com.example.schoolmanagement.Domain.Repository

import com.example.schoolmanagement.Data.Remote.HomeWorkRequest
import com.example.schoolmanagement.Data.Remote.HomeWorkResponse

interface  HomeWorkRepository {
    suspend fun getHomeworks(token: String): Result<List<HomeWorkResponse>>
    suspend fun postHomework(token: String, request: HomeWorkRequest): Result<Boolean>
    suspend fun deleteHomework(token: String, id: Int): Result<Boolean>

}