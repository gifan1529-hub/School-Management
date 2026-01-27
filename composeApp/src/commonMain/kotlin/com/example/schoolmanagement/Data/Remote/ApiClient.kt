package com.example.schoolmanagement.Data.Remote


import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object ApiClient {
    private const val BASE_URL = "http://10.20.0.93:8000/api/"

    val client = HttpClient {
        install(Logging) {
            level = LogLevel.BODY
        }

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }

        defaultRequest {
            header(HttpHeaders.Accept, "application/json")
        }
    }


    fun getUrl(endpoint: String) = "$BASE_URL$endpoint"
}