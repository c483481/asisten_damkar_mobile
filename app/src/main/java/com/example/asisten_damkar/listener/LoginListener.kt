package com.example.asisten_damkar.listener

import androidx.lifecycle.LiveData
import com.example.asisten_damkar.response.LoginResponse
import com.example.asisten_damkar.response.PemadamResponse

interface LoginListener {
    fun onStarted()
    fun onNotValid()
    fun fallback(success: LiveData<LoginResponse?>)
    fun fallbackPemadam(success: LiveData<PemadamResponse?>)
}