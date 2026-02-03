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

    suspend fun postAttendance(qrCode: String, token: String, lat: Double, long: Double): AttendanceResponse {
        return client.post(ApiClient.getUrl("attendance")) {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer ${token.trim()}")
            }
            setBody(AttendanceRequest(
                qr_code = qrCode,
                lat = lat,
                long = long
            ))
        }.body()
    }

    suspend fun getAttendanceHistory(token: String): AttendanceHistoryResponse {
        return client.get(ApiClient.getUrl("attendance/history")) {
                headers {
                append(HttpHeaders.Authorization, "Bearer ${token.trim()}")
            }
        }.body()
    }

    suspend fun getSchedules(token: String, `class`: String): ScheduleResponse {
        return client.get(ApiClient.getUrl("schedules")) {
            headers {
                append(HttpHeaders.Authorization, "Bearer ${token.trim()}")
            }
            url {
                parameters.append("class", `class`)
            }
        }.body()
    }

    suspend fun getClassAttendance(token: String, className: String): ClassAttendanceResponse {
        return client.get(ApiClient.getUrl("attendance/by-class")){
            headers {
                append(HttpHeaders.Authorization, "Bearer ${token.trim()}")
            }
            url {
                parameters.append("class", className)
            }
        }.body()
    }

    suspend fun postPermit(token: String, request: PermitRequest): PermitResponse {
         return client.post(ApiClient.getUrl("permits")) {
             contentType(ContentType.Application.Json)
             headers {
                 append(HttpHeaders.Authorization, "Bearer ${token.trim()}")
             }
             setBody(request)
         }.body()
    }
}