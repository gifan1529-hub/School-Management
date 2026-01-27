package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import kotlinx.coroutines.flow.first

data class UserDetails(
    val email : String,
    val name : String,
    val role : String,
    val isAlreadyAbsen : Boolean
)

data class AttendanceRequest(
    val latitude: Double,
    val longitude: Double,
    val deviceId: String
)

class getDetailUserUC (
    private val prefsManager: PrefsManager
){
    suspend operator fun invoke(): UserDetails? {
        val userEmail = prefsManager.getUserEmail.first()
        val userName = prefsManager.getUserName.first()
        val userRole = prefsManager.getUserRole.first()
        val userAbsen = prefsManager.getAbsenStatus.first()

        return if (!userEmail.isNullOrBlank()) {
            UserDetails(
                email = userEmail,
                name = userName,
                role = userRole,
                isAlreadyAbsen = userAbsen
            )
        } else {
            null
        }
    }
    }
