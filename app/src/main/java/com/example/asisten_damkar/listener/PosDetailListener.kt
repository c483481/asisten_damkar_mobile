package com.example.asisten_damkar.listener

import com.example.asisten_damkar.response.Truck

interface PosDetailListener {
    fun onGetResponse(items: List<Truck>)

    fun onFailedResponse()
}