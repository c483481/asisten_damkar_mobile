package com.example.asisten_damkar.listener

import com.example.asisten_damkar.response.TruckDetailResponse

interface TruckStatusListener {
    fun onSuccess(data: TruckDetailResponse)

    fun onFailed()

    fun loading()
    fun clear()
    fun update(data: Boolean, xid: String)
    fun delete(data: Boolean, xid: String)
}