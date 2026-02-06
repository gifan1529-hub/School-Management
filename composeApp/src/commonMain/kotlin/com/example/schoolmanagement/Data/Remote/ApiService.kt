package com.example.schoolmanagement.Data.Remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.parameters
import kotlin.text.append

class ApiService(private val client: HttpClient) {
    /**
     * ngambil data dari api
     */
    suspend fun getUser(token: String): UserApiResponse {
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

    suspend fun postAttendance(
        qrCode: String,
        token: String,
        lat: Double,
        long: Double
    ): AttendanceResponse {
        return client.post(ApiClient.getUrl("attendance")) {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer ${token.trim()}")
            }
            setBody(
                AttendanceRequest(
                    qr_code = qrCode,
                    lat = lat,
                    long = long
                )
            )
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
        return client.get(ApiClient.getUrl("attendance/by-class")) {
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

    suspend fun getPermits(token: String, all: Boolean = false): PermitResponse {
        return client.get(ApiClient.getUrl("permits")) {
            headers {
                append(HttpHeaders.Authorization, "Bearer ${token.trim()}")
            }
            url {
                if (all) parameters.append("all", "true")
            }
        }.body()
    }

    suspend fun getMySchedules(token: String): ScheduleResponse {
        return client.get(ApiClient.getUrl("my-schedules")) {
            headers {
                append(HttpHeaders.Authorization, "Bearer ${token.trim()}")
            }
        }.body()
    }

    suspend fun updatePermitStatus(token: String, id: Int, status: String): GenericResponse {
        return client.patch(ApiClient.getUrl("permits/$id/status")) {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer ${token.trim()}")
            }
            setBody(mapOf("status" to status))
        }.body()
    }

    suspend fun postAnnouncement(token: String, request: AlertRequest): AlertResponse {
        println("DEBUG API SEND - URL: ${ApiClient.getUrl("announcements")}")
        println("DEBUG API SEND - TOKEN: Bearer ${token.take(10)}...") // Cek token dikit aja
        println("DEBUG API SEND - BODY: $request")
        return client.post(ApiClient.getUrl("announcements")) {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer ${token.trim()}")
            }
            setBody(request)
        }.body()
    }

    suspend fun getAnnouncements(token: String): AlertListResponse {
        return client.get(ApiClient.getUrl("announcements")) {
            headers {
                append(HttpHeaders.Authorization, "Bearer ${token.trim()}")
            }
        }.body()
    }

    suspend fun getHomeworks(token: String): HomeWorkListResponse {
        return client.get(ApiClient.getUrl("homework")) {
            headers {
                append(HttpHeaders.Authorization, "Bearer ${token.trim()}")
            }
        }.body()
    }

    suspend fun postHomework(token: String, request: HomeWorkRequest): GenericResponse {
        return client.post(ApiClient.getUrl("homework")) {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer ${token.trim()}")
            }
            setBody(request)
        }.body()
    }

    suspend fun deleteHomework(token: String, id: Int): GenericResponse {
        return client.delete(ApiClient.getUrl("homework/$id")) {
            headers {
                append(HttpHeaders.Authorization, "Bearer ${token.trim()}")
            }
        }.body()
    }

    suspend fun submitHomework(
        token: String,
        homeworkId: Int,
        fileBytes: ByteArray,
        fileName: String
    ): HttpResponse {
        return client.submitFormWithBinaryData(
            url = ApiClient.getUrl("homework/$homeworkId/submit"),
            formData = formData {
                append("file", fileBytes, Headers.build {
                    append(HttpHeaders.ContentType, "application/octet-stream")
                    append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                })
            }
        ) {
            headers {
                append(HttpHeaders.Authorization, "Bearer ${token.trim()}")
            }
        }
    }

    suspend fun getHomeworkDetail(token: String, id: Int): HomeWorkDetailResponse {
        return client.get(ApiClient.getUrl("homework/$id")) {
            headers {
                append(HttpHeaders.Authorization, "Bearer ${token.trim()}")
            }
        }.body()
    }

    suspend fun getStats(token: String): AdminStatsResponse {
        return client.get(ApiClient.getUrl("dashboard/stats")) {
            headers {
                append(HttpHeaders.Authorization, "Bearer ${token.trim()}")
            }
        }.body()
    }

    suspend fun getAttendanceTrend(token: String): AttendanceTrendResponse {
        return client.get(ApiClient.getUrl("attendance/trend")) {
            headers {
                append(HttpHeaders.Authorization, "Bearer ${token.trim()}")
            }
        }.body()
    }

    suspend fun getAttendanceReport(
        token: String,
        role: String,
        `class`: String,
        status: String
    ): AttendanceReportResponse {
        return client.get(ApiClient.getUrl("attendance/report")) {
            headers {
                append(HttpHeaders.Authorization, "Bearer ${token.trim()}")
            }
            url {
                parameters.append("role", role)
                if (`class`.isNotBlank() && `class` != "Semua Kelas") {
                    parameters.append("class", `class`)
                }
                if (status.isNotBlank() && status != "Semua Status") {
                    parameters.append("status", status)
                }
            }
        }.body()
    }

    suspend fun giveGrade(token: String, submissionId: Int, grade: Int): GenericResponse {
        return client.patch(ApiClient.getUrl("homework/submissions/${submissionId}/grade")){
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer ${token.trim()}")
        }
            setBody(mapOf("grade" to grade))
        }.body()
    }

    suspend fun updateUser(token: String, userId: Int, role: String): GenericResponse {
        return client.patch(ApiClient.getUrl("users/$userId/role")) {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer ${token.trim()}")
            }
            setBody(mapOf("role" to role))
        }.body()
    }

    suspend fun getAllUsers(token: String, role: String? = null, search: String? = null): UserListResponse {
        return client.get(ApiClient.getUrl("users")) {
            headers { append(HttpHeaders.Authorization, "Bearer ${token.trim()}") }
            url {
                role?.let { parameters.append("role", it) }
                search?.let { parameters.append("search", it) }
            }
        }.body()
    }

    suspend fun deleteUser(token: String, userId: Int): GenericResponse {
        return client.delete(ApiClient.getUrl("users/$userId")) {
            headers { append(HttpHeaders.Authorization, "Bearer ${token.trim()}") }
        }.body()
    }

    suspend fun postUser(token: String, request: AddUserRequest): GenericResponse {
        return client.post(ApiClient.getUrl("users")) {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer ${token.trim()}")
            }
            setBody(request)
        }.body()
    }

}