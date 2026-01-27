package com.example.schoolmanagement.Data.Remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class ApiService(private val client: HttpClient) {
    /**
     * ngambil data dari api
     */
    suspend fun getUser(token: String): UserApiResponse{
        return client.get(ApiClient.getUrl("user")) {
            headers {
                append(HttpHeaders.Authorization, "Bearer ${token.trim()}")
            }
        }.body()
    }

    suspend fun login(request: LoginRequest): LoginResponse {
        return client.post(ApiClient.getUrl("login")) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun postAttendance(lat: Double, lon: Double): AttendanceResponse {
        return client.post("api/attendance") {
            setBody(mapOf("latitude" to lat, "longitude" to lon))
        }.body()
    }
}