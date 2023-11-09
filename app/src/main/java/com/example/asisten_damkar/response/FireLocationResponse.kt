package com.example.asisten_damkar.response

data class FireLocationResponse(
    val createdAt: Int,
    val modifiedBy: ModifyBy,
    val updatedAt: Int,
    val version: Int,
    val xid: String,
    val pos: PosResponse?,
    val lat: Double,
    val lng: Double,
    val status: String,
)