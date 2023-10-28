package com.example.asisten_damkar.response

data class Response<T>(
    val success: String,
    val message: String,
    val data: T
)