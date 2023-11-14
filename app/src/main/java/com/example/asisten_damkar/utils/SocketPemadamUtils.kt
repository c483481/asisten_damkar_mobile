package com.example.asisten_damkar.utils

import android.util.Log
import com.example.asisten_damkar.listener.HomePemadamSocketListener
import com.example.asisten_damkar.response.FireLocationResponse
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import org.json.JSONString

class SocketPemadamUtils {
    private var socket: Socket = IO.socket("${getBaseUrl()}/pemadam")

    fun connect(posXid: String) {
        socket.connect()
        socket.emit("join", posXid)
    }

    fun socketEvent(listener: HomePemadamSocketListener) {
        socket.on("connected"){
            listener.onConnected()
        }

        socket.on("fire-location") {args ->
            val fireLocationArgs = args[0] as JSONObject
            listener.onPushFireLocation(FireLocationResponse.fromJsonObject(fireLocationArgs))
        }
    }
}