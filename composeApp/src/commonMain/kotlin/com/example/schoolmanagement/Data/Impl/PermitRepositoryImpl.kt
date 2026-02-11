package com.example.schoolmanagement.Data.Impl

import com.example.schoolmanagement.Data.Remote.ApiService
import com.example.schoolmanagement.Data.Remote.PermitRequest
import com.example.schoolmanagement.Data.Remote.PermitResponse
import com.example.schoolmanagement.Domain.Model.PermitData
import com.example.schoolmanagement.Domain.Repository.PermitRepository
import io.ktor.client.plugins.ResponseException

class PermitRepositoryImpl(
    private val apiService: ApiService
) : PermitRepository {
    private fun handleException(e: Exception): Exception {
        return if (e is ResponseException) {
            val status = e.response.status.value
            val message = when (status) {
                400 -> "Permintaan tidak valid (400)"
                401 -> "Sesi berakhir, silakan login ulang (401)"
                403 -> "Anda tidak memiliki akses (403)"
                404 -> "Data tidak ditemukan (404)"
                500 -> "Server sedang bermasalah (500)"
                else -> "Terjadi kesalahan server: $status"
            }
            Exception(message)
        } else {
            Exception("Masalah koneksi: ${e.message}")
        }
    }

    override suspend fun submitPermit(token: String, request: PermitRequest): Result<Boolean> {
        return try {
            val response = apiService.postPermit(token, request)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(handleException(e))
        }
    }

    override suspend fun getPermits(token: String, all: Boolean): Result<List<PermitData>> {
        return try {
            val response = apiService.getPermits(token, all)
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(handleException(e))
        }
    }

    override suspend fun updatePermitStatus(token: String, id: Int, status: String): Result<Boolean> {
        return try {
            apiService.updatePermitStatus(token, id, status)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(handleException(e))
        }
    }
}