package com.example.schoolmanagement.Data.Impl

import com.example.schoolmanagement.Data.Remote.AddUserRequest
import com.example.schoolmanagement.Data.Remote.ApiService
import com.example.schoolmanagement.Domain.Repository.UpdateUserRepository
import com.example.schoolmanagement.Utils.HandleException
import com.example.schoolmanagement.Domain.Model.UserDatas

class UpdateUserRepositoryImpl (
    private val apiService: ApiService
): UpdateUserRepository {
    val exceptionHandler = HandleException()
    override suspend fun updateUser(token: String, userId: Int, role: String): Result<Boolean> {
        return try {
            apiService.updateUser(token, userId, role)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(exceptionHandler.handleException(e))
        }
    }

    override suspend fun getAllUsers(token: String, role: String?, search: String?): Result<List<UserDatas>> {
        return try {
            val response = apiService.getAllUsers(token, role, search)
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(exceptionHandler.handleException(e))
        }
    }

    override suspend fun deleteUser(token: String, userId: Int): Result<Boolean> {
        return try {
            apiService.deleteUser(token, userId)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(exceptionHandler.handleException(e))
        }
    }

    override suspend fun addUser(token: String, request: AddUserRequest): Result<Boolean> {
        return try {
            apiService.postUser(token, request)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(exceptionHandler.handleException(e))
        }
    }
}