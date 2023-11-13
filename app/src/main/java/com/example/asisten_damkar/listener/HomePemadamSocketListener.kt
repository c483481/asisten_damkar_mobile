package com.example.asisten_damkar.listener

import com.example.asisten_damkar.response.FireLocationResponse

interface HomePemadamSocketListener {
    fun onConnected()

    fun onPushFireLocation(data: FireLocationResponse)
}