package com.example.asisten_damkar.listener

import com.example.asisten_damkar.response.PosResponse
import com.example.asisten_damkar.response.ResponseList

interface MonitoringListener {
    fun onSuccessFetch(data: ResponseList<PosResponse>)
    fun onFailed()
}