package com.example.asisten_damkar.listener

import androidx.lifecycle.LiveData

interface AddTruckListener {
    fun onCallbackResponse(data: LiveData<Boolean>)

    fun onInvalidPlat()
}