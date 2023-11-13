package com.example.asisten_damkar.listener

import androidx.lifecycle.LiveData
import com.example.asisten_damkar.response.FireLocationResponse
import com.example.asisten_damkar.response.ResponseList

interface HomePemadamFragmentListener {
    fun onCallbackFireList(data: LiveData<ResponseList<FireLocationResponse>?>)
}
