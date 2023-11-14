package com.example.asisten_damkar.listener

interface MapsUpdateSocketListener {
    fun onConnected(lat: Double, lng: Double)
}