package com.example.schoolmanagement.Data.Impl

import com.example.schoolmanagement.Data.Remote.AlertRequest
import com.example.schoolmanagement.Data.Remote.ApiService
import com.example.schoolmanagement.Domain.Repository.AnnouncmentRepository
import io.ktor.client.plugins.ResponseException
import com.example.schoolmanagement.Data.Remote.AlertResponse
import com.example.schoolmanagement.Domain.Model.AlertData


class AnnouncmentRepositoryImpl (
    private val apiService: ApiService
): AnnouncmentRepository {

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

    override suspend fun postAnnouncement(token: String, request: AlertRequest): Result<AlertResponse> {
        return try {
            val response = apiService.postAnnouncement(token, request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(handleException(e))
        }
    }

    override suspend fun getAnnouncements(token: String): Result<List<AlertData>> {
        return try {
            val response = apiService.getAnnouncements(token)
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(handleException(e))
        }
    }
}