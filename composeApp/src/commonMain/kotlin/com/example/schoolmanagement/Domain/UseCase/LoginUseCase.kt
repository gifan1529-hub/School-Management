package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Remote.ApiService
import com.example.schoolmanagement.Data.Remote.LoginRequest
import com.example.schoolmanagement.Data.Remote.LoginResponse

sealed class LoginResult {
    data class Success(val response: LoginResponse) : LoginResult()
    data class Error(val message: String) : LoginResult()
    data class Failure(val error: Exception) : LoginResult()
}

class LoginUC (
    private val apiService: ApiService
) {
    suspend operator fun invoke (email: String, password: String): LoginResult {
        if (email.isBlank()) return LoginResult.Error("Email gabisa koosn")
        if (password.isBlank()) return LoginResult.Error("Password gabisa kojsn")

        return try {
            val response = apiService.login(LoginRequest(email, password))

            LoginResult.Success(response)
        } catch (e: Exception) {
            LoginResult.Failure(e)
        }
    }

}