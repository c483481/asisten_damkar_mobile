package com.example.asisten_damkar.response

import org.json.JSONObject

data class PosResponse(
    val active: Boolean,
    val createdAt: Int,
    val location: Location,
    val modifiedBy: ModifyBy,
    val name: String,
    val updatedAt: Int,
    val version: Int,
    val xid: String
) {
    companion object {
        fun fromJsonObject(json: JSONObject): PosResponse {
            return PosResponse(
                xid = json.getString("xid"),
                createdAt = json.getInt("createdAt"),
                updatedAt = json.getInt("updatedAt"),
                name = json.getString("name"),
                version = json.getInt("version"),
                modifiedBy = ModifyBy.fromJsonObject(json.getJSONObject("modifiedBy")),
                active = json.getBoolean("active"),
                location = Location.fromJsonObject(json.getJSONObject("location"))
            )
        }
    }
}