package com.example.schoolmanagement.Domain.Repository

import com.example.schoolmanagement.Domain.Model.ViolationData

interface ViolationRepository {
    suspend fun getViolations(token: String): Result<List<ViolationData>>

    suspend fun postViolation(
        token: String,
        studentId: Int,
        violationName: String,
        category: String,
        points: Int,
        description: String?,
        imageBytes: ByteArray?,
        imageName: String?
    ): Result<Boolean>
}