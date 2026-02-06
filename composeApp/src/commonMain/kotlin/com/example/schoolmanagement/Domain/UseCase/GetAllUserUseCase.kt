package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Domain.Model.UserDatas
import com.example.schoolmanagement.Domain.Repository.UpdateUserRepository
import kotlinx.coroutines.flow.first

class GetAllUserUseCase (
    private val repository: UpdateUserRepository,
    private val prefsManager: PrefsManager
) {
    suspend operator fun invoke(role: String? = null, search: String? = null): Result<List<UserDatas>> {
        return try {
            val token = prefsManager.getAuthToken.first() ?: ""
                if (token.isEmpty()) return Result.failure(Exception("Sesi berakhir, silakan login ulang"))
                repository.getAllUsers(token, role, search)
        } catch (e: Exception) {
            Result.failure(e)
        }
        }
    }