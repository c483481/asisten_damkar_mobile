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

fun List<Item>.removeByXid(xid: String): List<Item> {
    val mutableList = toMutableList()
    val index = mutableList.indexOfFirst { it.xid == xid }
    if (index != -1) {
        mutableList.removeAt(index)
    }
    return mutableList.toList()
}

fun List<Item>.updateByXid(xid: String): List<Item> {
    val mutableList = toMutableList()
    val index = mutableList.indexOfFirst { it.xid == xid }
    if (index != -1) {
        mutableList[index] = mutableList[index].copy(active = !mutableList[index].active)
    }
    return mutableList.toList()
}