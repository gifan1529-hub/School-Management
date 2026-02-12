package com.example.schoolmanagement.Domain.Repository

import com.example.schoolmanagement.Data.Remote.PermitRequest
import com.example.schoolmanagement.Data.Remote.PermitResponse
import com.example.schoolmanagement.Domain.Model.PermitData

interface PermitRepository {
    suspend fun submitPermit(
        token: String,
        type: String,
        startDate: String,
        endDate: String,
        reason: String,
        timeIn: String,
        timeOut: String,
        fileBytes: ByteArray?,
        fileName: String?
    ): Result<Boolean>
    suspend fun getPermits(token: String, all: Boolean): Result<List<PermitData>>
    suspend fun updatePermitStatus(token: String, id: Int, status: String): Result<Boolean>
}