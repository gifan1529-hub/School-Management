package com.example.schoolmanagement.Domain.Repository

import com.example.schoolmanagement.Data.Remote.AddUserRequest
import com.example.schoolmanagement.Domain.Model.UserDatas

interface UpdateUserRepository {
    suspend fun updateUser(token: String, userId: Int, role: String): Result<Boolean>
    suspend fun getAllUsers(token: String, role: String? = null, search: String? = null): Result<List<UserDatas>>
    suspend fun deleteUser(token: String, userId: Int): Result<Boolean>
    suspend fun addUser(token: String, request: AddUserRequest): Result<Boolean>}