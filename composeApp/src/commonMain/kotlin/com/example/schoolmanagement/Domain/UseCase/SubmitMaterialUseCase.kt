package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Domain.Repository.MaterialRepository
import kotlinx.coroutines.flow.first

class SubmitMaterialUseCase (
    private val repository: MaterialRepository,
    private val prefsManager: PrefsManager
) {
    suspend operator fun invoke(
        title: String,
        description: String,
        subject: String,
        type: String,
        fileBytes: ByteArray? = null,
        fileName: String? = null,
        linkContent: String? = null
    ): Result<Boolean>{
        return try {
            val token = prefsManager.getAuthToken.first() ?: ""
            val userClass = prefsManager.getClass.first()

            if (token.isEmpty()) return Result.failure(Exception("Sesi berakhir"))

            repository.submitMaterial(
                token = token,
                title = title,
                description = description,
                subject = subject,
                className = userClass,
                type = type,
                fileBytes = fileBytes,
                fileName = fileName,
                linkContent = linkContent
            )
        }  catch (e: Exception) {
            Result.failure(e)
        }
    }
}