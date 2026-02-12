package com.example.schoolmanagement.Data.Impl

import com.example.schoolmanagement.Data.Remote.ApiService
import com.example.schoolmanagement.Domain.Repository.MaterialRepository
import com.example.schoolmanagement.Utils.HandleException
import com.example.schoolmanagement.Domain.Model.MaterialData

class MaterialRepositoryImpl (
    private val apiService: ApiService
): MaterialRepository {
    private val exceptionHandler = HandleException()

    override suspend fun getMaterials(token: String): Result<List<MaterialData>> {
        return try {
            val response = apiService.getMaterials(token)
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(exceptionHandler.handleException(e))
        }
    }

    override suspend fun submitMaterial(
        token: String,
        title: String,
        description: String,
        subject: String,
        className: String,
        type: String,
        fileBytes: ByteArray?,
        fileName: String?,
        linkContent: String?
    ): Result<Boolean> {
        return try {
            apiService.postMaterial(
                token, title, description, subject, className, type, fileBytes, fileName, linkContent
            )
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(exceptionHandler.handleException(e))
        }
    }

    override suspend fun deleteMaterial(token: String, id: Int): Result<Boolean> {
        return try {
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(exceptionHandler.handleException(e))
        }
    }
}