package com.example.asisten_damkar.response

import org.json.JSONObject

data class Location(
    val lat: Double,
    val lng: Double
) {
    companion object {
        fun fromJsonObject(json: JSONObject): Location {
            return Location(
                lat = json.getDouble("lat"),
                lng = json.getDouble("lng")
            )
        }
    }
}