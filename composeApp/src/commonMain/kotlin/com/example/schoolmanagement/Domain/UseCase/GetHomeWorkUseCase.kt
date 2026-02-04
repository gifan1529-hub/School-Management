package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Data.Remote.HomeWorkListResponse
import com.example.schoolmanagement.Data.Remote.HomeWorkResponse
import com.example.schoolmanagement.Domain.Repository.HomeWorkRepository
import kotlinx.coroutines.flow.first

class GetHomeWorkUseCase (
    private val repository: HomeWorkRepository,
    private val prefsManager: PrefsManager
) {
    suspend operator fun invoke(): Result<List<HomeWorkResponse>> {
        return try {
            val token = prefsManager.getAuthToken.first() ?: ""

            if (token.isEmpty()) {
                return Result.failure(Exception("Sesi berakhir, silakan login ulang"))
            }
            repository.getHomeworks(token)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}