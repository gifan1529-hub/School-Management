package com.example.schoolmanagement.ViewModel

import androidx.lifecycle.ViewModel
import com.example.schoolmanagement.Data.Local.PrefsManager

class AuthViewModel (
    private val prefs: PrefsManager
): ViewModel() {
    suspend fun isLoggedIn(): Boolean {
        return prefs.isLoggedIn()
    }
}