package com.example.asisten_damkar.response

data class Item(
    val active: Boolean,
    val createdAt: Int,
    val modifiedBy: ModifyBy,
    val name: String,
    val updatedAt: Int,
    val version: Int,
    val xid: String
)