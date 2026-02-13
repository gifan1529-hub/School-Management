package com.example.schoolmanagement.Domain.Model

data class UserDetails(
    val id : Int,
    val email : String,
    val name : String,
    val role : String,
    val isAlreadyAbsen : Boolean,
    val phone : String,
    val nisn : String,
    val kelas : String,
    val address : String,
)