package com.example.schoolmanagement.Data.Remote

import com.example.schoolmanagement.Domain.Model.UserData
import com.example.schoolmanagement.Domain.Model.UserDatas
import kotlinx.serialization.Serializable

@Serializable
data class UserListResponse (
    val message: String,
    val data: List<UserDatas>
)