package com.example.schoolmanagement.DI

import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

actual class FcmHelper actual constructor() {
    actual suspend fun getFcmToken(): String? {
        return try {
            FirebaseMessaging.getInstance().token.await()
        } catch (e: Exception) {
            null
        }
    }
}