package com.example.schoolmanagement.Domain.Model

import com.example.schoolmanagement.Domain.Model.UserDataa
import kotlinx.serialization.Serializable

@Serializable
data class LogData (
    val id: Int,
    val user_id: Int,
    val action: String,
    val module: String,
    val details: String,
    val created_at: String,
    val user: UserDataa ?= null
)
