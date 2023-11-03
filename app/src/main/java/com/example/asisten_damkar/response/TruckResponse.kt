package com.example.asisten_damkar.response

data class TruckResponse(
    val active: Boolean,
    val createdAt: Int,
    val modifiedBy: ModifyBy,
    val plat: String,
    val updatedAt: Int,
    val version: Int,
    val xid: String
)