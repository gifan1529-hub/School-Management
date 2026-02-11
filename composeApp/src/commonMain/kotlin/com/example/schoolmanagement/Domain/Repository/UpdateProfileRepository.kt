package com.example.schoolmanagement.Domain.Repository

import com.example.schoolmanagement.Data.Remote.UpdateProfileRequest

interface UpdateProfileRepository {
    suspend fun updateProfile(token: String, request: UpdateProfileRequest): Result<Boolean>
}