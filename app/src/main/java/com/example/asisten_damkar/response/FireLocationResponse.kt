package com.example.asisten_damkar.response

import org.json.JSONObject

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
    val arriveAt: Int?
){
    companion object {
        fun fromJsonObject(json: JSONObject): FireLocationResponse {
            val arriveAtJson = json.opt("arriveAt")
            val arriveAt = if (arriveAtJson != null && !arriveAtJson.toString().equals("null", ignoreCase = true)) {
                arriveAtJson as Int
            } else {
                null
            }
            return FireLocationResponse(
                createdAt = json.getInt("createdAt"),
                modifiedBy = ModifyBy.fromJsonObject(json.getJSONObject("modifiedBy")),
                updatedAt = json.getInt("updatedAt"),
                version = json.getInt("version"),
                xid = json.getString("xid"),
                pos = if (json.has("pos")) PosResponse.fromJsonObject(json.getJSONObject("pos")) else null,
                lat = json.getDouble("lat"),
                lng = json.getDouble("lng"),
                status = json.getString("status"),
                arriveAt = arriveAt
            )
        }
    }
}