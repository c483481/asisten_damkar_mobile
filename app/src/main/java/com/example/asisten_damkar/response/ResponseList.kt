package com.example.asisten_damkar.response

data class ResponseList<T>(
    val items: Array<T>,
    val count: Int
)
