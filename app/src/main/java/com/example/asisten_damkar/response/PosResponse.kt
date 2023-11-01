package com.example.asisten_damkar.response

data class PosResponse(
    val active: Boolean,
    val createdAt: Int,
    val location: Location,
    val modifiedBy: String,
    val name: String,
    val updatedAt: Int,
    val version: Int,
    val xid: String
)