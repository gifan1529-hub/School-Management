package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Data.Remote.HomeWorkRequest
import com.example.schoolmanagement.Domain.Repository.HomeWorkRepository
import kotlinx.coroutines.flow.first

class PostHomeWorkUseCase (
    private val repository: HomeWorkRepository,
    private val prefsManager: PrefsManager
) {
    suspend operator fun invoke(
        subject: String,
        targetClass: String,
        title: String,
        description: String,
        deadline: String
    ): Result<Boolean> {
        return try {
            val token = prefsManager.getAuthToken.first() ?: ""

            if (token.isEmpty()) {
                return Result.failure(Exception("Sesi berakhir, silakan login ulang"))
            }

            val request = HomeWorkRequest(
                subject = subject,
                `class` = targetClass,
                title = title,
                description = description,
                deadline = deadline
            )

            repository.postHomework(token, request)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}