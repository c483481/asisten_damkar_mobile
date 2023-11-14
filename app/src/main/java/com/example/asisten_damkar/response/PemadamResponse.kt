package com.example.asisten_damkar.response

data class PemadamResponse(
    val createdAt: Int,
    val modifiedBy: ModifyBy,
    val updatedAt: Int,
    val version: Int,
    val xid: String,
    val pos: PosResponse?,
    val truck: TruckResponse?
)