package com.example.asisten_damkar.response

data class LoginResponse(
    val createdAt: Int,
    val key: Key,
    val modifiedBy: ModifyBy,
    val role: String,
    val tagRole: String,
    val updatedAt: Int,
    val username: String,
    val version: Int,
    val xid: String
)