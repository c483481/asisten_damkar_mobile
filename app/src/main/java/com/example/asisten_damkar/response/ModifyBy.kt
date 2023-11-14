package com.example.asisten_damkar.response

import org.json.JSONObject

data class ModifyBy(
    val xid: String,
    val username: String
) {
    companion object {
        fun fromJsonObject(json: JSONObject): ModifyBy {
            return ModifyBy(
                xid = json.getString("xid"),
                username = json.getString("username")
            )
        }
    }
}
