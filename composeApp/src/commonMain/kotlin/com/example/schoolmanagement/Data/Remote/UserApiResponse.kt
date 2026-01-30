package com.example.schoolmanagement.Data.Remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserApiResponse (

    @SerialName("id")
    val id: Int,

    @SerialName("name")
    val name: String,

    @SerialName("email")
    val email: String,

//    @SerialName("password")
//    val password: String,

    @SerialName("role")
    val role: String,

    @SerialName("class")
    val kelas: String?,

    @SerialName("nisn")
    val nisn: String?,

    @SerialName("phone")
    val phone: String?

)