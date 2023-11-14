package com.example.asisten_damkar.utils

import com.example.asisten_damkar.listener.MapsUpdateSocketListener
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

class SocketMapsUtils {
    private var socket: Socket = IO.socket("${getBaseUrl()}/maps")

    fun connect(xid: String) {
        socket.connect()
        socket.emit("join", xid)
    }

    fun socketEventLimit(listener: MapsUpdateSocketListener) {
        socket.on("changeLocation") {
            val json = it[0] as JSONObject
            val lat = json.getDouble("lat")
            val lng = json.getDouble("lng")
            listener.onConnected(lat, lng)
        }
    }

    fun updatePemadamLocation(lat: Double, lng: Double, fireLocationXid: String) {
        val jsonCoordinate = JSONObject()
        jsonCoordinate.put("lat", lat)
        jsonCoordinate.put("lng", lng)
        jsonCoordinate.put("fireLocationXid", fireLocationXid)
        socket.emit("updateLocation", jsonCoordinate)
    }
}