package com.example.asisten_damkar.response

data class TruckDetailResponse(
    val active: Boolean,
    val createdAt: Int,
    val items: List<Item>,
    val modifiedBy: ModifyBy,
    val plat: String,
    val updatedAt: Int,
    val version: Int,
    val xid: String
)