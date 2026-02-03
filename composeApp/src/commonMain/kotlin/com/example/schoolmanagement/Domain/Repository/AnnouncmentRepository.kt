package com.example.schoolmanagement.Domain.Repository

import com.example.schoolmanagement.Data.Remote.AlertRequest
import com.example.schoolmanagement.Data.Remote.AlertResponse
import com.example.schoolmanagement.Domain.Model.AlertData

interface AnnouncmentRepository {
    suspend fun getAnnouncements(token: String): Result<List<AlertData>>
    suspend fun postAnnouncement(token: String, request: AlertRequest): Result<AlertResponse>
}