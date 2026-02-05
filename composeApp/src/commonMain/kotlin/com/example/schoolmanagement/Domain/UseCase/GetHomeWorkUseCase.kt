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
        println("DEBUG GURU: Memanggil GetHomeWorkUseCase")
        return try {
            val token = prefsManager.getAuthToken.first() ?: ""
            val response = repository.getHomeworks(token)
            println("DEBUG GURU: $response")

            if (token.isEmpty()) {
                return Result.failure(Exception("Sesi berakhir, silakan login ulang"))
            }
            response
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}