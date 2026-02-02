package com.example.schoolmanagement.DI

expect class Location {
    suspend fun getCurrentLocation(): Pair<Double, Double>?
}