package com.example.schoolmanagement.DI

import android.content.Context
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await

actual class  Location (private val context: Context) {

    actual suspend fun getCurrentLocation(): Pair<Double, Double>? {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        return try {
            val location = fusedLocationClient.lastLocation.await()

            if (location != null) {
                Pair(location.latitude, location.longitude)
            } else {
                null
            }
        } catch (e: Exception) {
            println("DEBUG LOCATION: ${e.message}")
            null
        }
    }
}