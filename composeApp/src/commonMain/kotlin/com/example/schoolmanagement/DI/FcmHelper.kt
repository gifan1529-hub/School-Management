package com.example.schoolmanagement.DI

expect class FcmHelper() {
    suspend fun getFcmToken(): String?
}