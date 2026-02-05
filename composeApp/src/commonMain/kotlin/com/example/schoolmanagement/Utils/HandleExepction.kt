package com.example.schoolmanagement.Utils

import io.ktor.client.plugins.ResponseException

class HandleException {
    fun handleException(e: Exception): Exception {
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
            Exception("Terjadi kesalahan: ${e.message}")
        }
    }
}