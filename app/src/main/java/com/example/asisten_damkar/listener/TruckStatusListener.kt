package com.example.asisten_damkar.listener

import com.example.asisten_damkar.response.TruckDetailResponse

interface TruckStatusListener {
    fun onSuccess(data: TruckDetailResponse)

    fun onFailed()
}