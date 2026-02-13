package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Domain.Repository.ViolationRepository
import kotlinx.coroutines.flow.first

class PostViolationUseCase (
    private val repository: ViolationRepository,
    private val prefsManager: PrefsManager
) {
    suspend operator fun invoke(
        studentId: Int,
        name: String,
        category: String,
        points: Int,
        description: String?,
        imageBytes: ByteArray?,
        imageName: String?
    ): Result<Boolean>{
        val token = prefsManager.getAuthToken.first() ?: ""
        return repository.postViolation(
            token,
            studentId,
            name,
            category,
            points,
            description,
            imageBytes,
            imageName
        )
    }
}