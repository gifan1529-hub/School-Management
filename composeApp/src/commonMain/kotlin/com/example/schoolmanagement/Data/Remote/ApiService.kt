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
import io.ktor.client.statement.bodyAsText
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

    suspend fun postPermit(
        token: String,
        type: String,
        startDate: String,
        endDate: String,
        reason: String,
        timeIn: String,
        timeOut: String,
        fileBytes: ByteArray?,
        fileName: String?
    ): SubmitPermitResponse {
        return client.submitFormWithBinaryData(
            url = ApiClient.getUrl("permits"),
            formData = formData {
                append("type", type)
                append("start_date", startDate)
                append("end_date", endDate)
                append("reason", reason)
                append("timeIn", timeIn)
                append("timeOut", timeOut)

                if (fileBytes != null && fileName != null) {
                    append("image", fileBytes, Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                    })
                }
            }
        ) {
            headers {
                append(HttpHeaders.Authorization, "Bearer ${token.trim()}")
            }
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

    suspend fun getGradeSummary(token: String): GradeSummaryResponse {
        return client.get(ApiClient.getUrl("homework/summary")) {
            headers {
                append(HttpHeaders.Authorization, "Bearer ${token.trim()}")
            }
        }.body()
    }

    suspend fun getMyActivities (token: String, page: Int = 1): ActivityLogResponse {
        return client.get(ApiClient.getUrl("my-activities")) {
            headers { append(HttpHeaders.Authorization, "Bearer ${token.trim()}") }
            url { parameters.append("page", page.toString())}
        }.body()
    }

    suspend fun getAllActivities (token: String, page: Int = 1): ActivityLogResponse {
        return client.get(ApiClient.getUrl("activity-logs")) {
            headers { append(HttpHeaders.Authorization, "Bearer ${token.trim()}") }
            url { parameters.append("page", page.toString())}
        }.body()
    }

    suspend fun getUnreadActivity (token: String): Int{
        val response: Map<String, Int> = client.get(ApiClient.getUrl("activity-logs/unread-count")) {
            headers { append(HttpHeaders.Authorization, "Bearer ${token.trim()}") }
        }.body()
        return response["unread_count"] ?: 0
    }

    suspend fun markAllActivitiesAsRead(token: String): GenericResponse {
        return client.post(ApiClient.getUrl("activity-logs/mark-as-read")) {
            headers { append(HttpHeaders.Authorization, "Bearer ${token.trim()}") }
        }.body()
    }

    suspend fun updateProfile(token: String, request: UpdateProfileRequest): UpdateProfileResponse {
        return client.post(ApiClient.getUrl("user/update-profile")) {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer ${token.trim()}")
            }
            setBody(request)
        }.body()
    }

    suspend fun sendFcmToken(token: String, fcmToken: String): GenericResponse {
        return client.post(ApiClient.getUrl("fcm-token")) {
            contentType(ContentType.Application.Json)
            headers { append(HttpHeaders.Authorization, "Bearer ${token.trim()}") }
            setBody(mapOf("token" to fcmToken, "device_type" to "android"))
        }.body()
    }

    suspend fun getMyGrades(token: String, subject: String? = null): MyGradeResponse {
        val response =  client.get(ApiClient.getUrl("my-grades")) {
            headers { append(HttpHeaders.Authorization, "Bearer ${token.trim()}") }
            url { subject?.let { parameters.append("subject", it) } }
        }
        println("DEBUG JSON DARI LARAVEL: ${response.bodyAsText()}")
        return response.body()
    }

    suspend fun getAdminTeacherSchedule(token: String, teacherId: Int? = null): ScheduleResponse {
        return client.get(ApiClient.getUrl("admin/teacher-schedule")) {
            headers {append(HttpHeaders.Authorization, "Bearer ${token.trim()}")}
            url {
                teacherId?.let { parameters.append("teacher_id", it.toString()) }
            }
        }.body()
    }

    suspend fun getMaterials(token: String): MaterialListResponse {
        return client.get(ApiClient.getUrl("materials")) {
            headers { append(HttpHeaders.Authorization, "Bearer ${token.trim()}") }
        }.body()
    }

    suspend fun postMaterial(
        token: String,
        title: String,
        description: String,
        subject: String,
        className: String,
        type: String,
        fileBytes: ByteArray? = null,
        fileName: String? = null,
        linkContent: String? = null
    ): GenericResponse {
        return client.submitFormWithBinaryData(
            url = ApiClient.getUrl("materials"),
            formData = formData {
                append("title", title)
                append("description", description)
                append("subject", subject)
                append("class", className)
                append("type", type)

                if (type == "file" || type == "video") {
                    fileBytes?.let {
                        append("file", it, Headers.build {
                            append(HttpHeaders.ContentType, "application/octet-stream")
                            append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                        })
                    }
                } else {
                    append("content", linkContent ?: "")
                }
            }
        ) {
            headers { append(HttpHeaders.Authorization, "Bearer ${token.trim()}") }
        }.body()
    }

    suspend fun getViolations(token: String): ViolationListResponse {
        return client.get(ApiClient.getUrl("violations")) {
            headers { append(HttpHeaders.Authorization, "Bearer ${token.trim()}") }
        }.body()
    }

    suspend fun postViolation(
        token: String,
        studentId: Int,
        name: String,
        category: String,
        points: Int,
        description: String?,
        imageBytes: ByteArray?,
        imageName: String?
    ): GenericResponse {
        return client.submitFormWithBinaryData(
            url = ApiClient.getUrl("violations"),
            formData = formData {
                append("student_id", studentId)
                append("violation_name", name)
                append("category", category)
                append("points", points)
                append("description", description ?: "")
                imageBytes?.let {
                    append("image", it, Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(HttpHeaders.ContentDisposition, "filename=\"$imageName\"")
                    })
                }
            }
        ) {
            headers { append(HttpHeaders.Authorization, "Bearer ${token.trim()}") }
        }.body()
    }

    suspend fun getDiscussions(token: String, homeworkId: Int): DiscussionListResponse {
        return client.get(ApiClient.getUrl("homework/$homeworkId/discussions")) {
            headers { append(HttpHeaders.Authorization, "Bearer ${token.trim()}") }
        }.body()
    }

    suspend fun postDiscussion(token: String, homeworkId: Int, message: String): GenericResponse {
        return client.post(ApiClient.getUrl("homework/$homeworkId/discussions")) {
            contentType(ContentType.Application.Json)
            headers { append(HttpHeaders.Authorization, "Bearer ${token.trim()}") }
            setBody(mapOf("message" to message))
        }.body()
    }

    suspend fun getParentDashboard(token: String): ParentDashboardResponse {
        return client.get(ApiClient.getUrl("parent/dashboard")) {
            headers { append(HttpHeaders.Authorization, "Bearer ${token.trim()}") }
        }.body()
    }

    suspend fun getChildAttendance(token: String, month: Int, year: Int): ChildAttendanceResponse {
        return client.get(ApiClient.getUrl("parent/attendance")) {
            headers { append(HttpHeaders.Authorization, "Bearer ${token.trim()}") }
            url {
                parameters.append("month", month.toString())
                parameters.append("year", year.toString())
            }
        }.body()
    }

    suspend fun logout(token: String): GenericResponse{
        return client.post(ApiClient.getUrl("logout")) {
            headers {
                append(HttpHeaders.Authorization, "Bearer ${token.trim()}")
            }
        }.body()
    }
}