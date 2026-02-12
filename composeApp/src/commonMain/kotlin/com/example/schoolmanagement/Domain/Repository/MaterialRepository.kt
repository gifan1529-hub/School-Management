package com.example.schoolmanagement.Domain.Repository

import com.example.schoolmanagement.Domain.Model.MaterialData

interface MaterialRepository {
    suspend fun getMaterials(token: String): Result<List<MaterialData>>

    suspend fun submitMaterial(
        token: String,
        title: String,
        description: String,
        subject: String,
        className: String,
        type: String,
        fileBytes: ByteArray? = null,
        fileName: String? = null,
        linkContent: String? = null
    ): Result<Boolean>

    suspend fun deleteMaterial(token: String, id: Int): Result<Boolean>

}