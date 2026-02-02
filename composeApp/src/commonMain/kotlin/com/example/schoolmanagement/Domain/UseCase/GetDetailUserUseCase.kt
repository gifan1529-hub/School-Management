package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Domain.Model.UserDetails
import kotlinx.coroutines.flow.first


class getDetailUserUC (
    private val prefsManager: PrefsManager
) {
    suspend operator fun invoke(): UserDetails? {
        return prefsManager.getUserData()
    }
}
