package com.example.schoolmanagement.Data.Impl

import com.example.schoolmanagement.Data.Remote.ApiService
import com.example.schoolmanagement.Domain.Model.ViolationData
import com.example.schoolmanagement.Domain.Repository.ViolationRepository
import com.example.schoolmanagement.Utils.HandleException

class ViolationRepositoryImpl (
    private val apiService: ApiService
) : ViolationRepository {
    val exceptionHandler = HandleException()

    override suspend fun getViolations(token: String): Result<List<ViolationData>> {
        return try {
            val response = apiService.getViolations(token)
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(exceptionHandler.handleException(e))
        }
    }

    override suspend fun postViolation(
        token: String,
        studentId: Int,
        violationName: String,
        category: String,
        points: Int,
        description: String?,
        imageBytes: ByteArray?,
        imageName: String?
    ): Result<Boolean>{
        return try {
            apiService.postViolation(
                token,
                studentId,
                violationName,
                category,
                points,
                description,
                imageBytes,
                imageName
            )
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(exceptionHandler.handleException(e))
        }
    }

//    override suspend fun deleteViolation(token: String, id: Int): Result<Boolean> {
//        return try {
//            apiService.deleteViolation(token, id)
//            Result.success(true)
//        } catch (e: Exception) {
//            Result.failure(exceptionHandler.handleException(e))
//        }
//    }
}