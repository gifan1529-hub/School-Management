package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager

class LogoutUseCase (
    private val prefsManager: PrefsManager
) {
    suspend operator fun invoke() {
        prefsManager.clearSession()
    }
}