package com.example.asisten_damkar.utils

import android.util.Log
import com.example.asisten_damkar.listener.HomePemadamSocketListener
import com.example.asisten_damkar.response.FireLocationResponse
import io.socket.client.IO
import io.socket.client.Socket

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
            val fireLocation: FireLocationResponse = args[0] as FireLocationResponse

        }
    }

    fun disconnect() {
        socket.disconnect()
    }

    fun isConnected(): Boolean {
        return socket.connected()
    }
}