package com.example.asisten_damkar.listener

import androidx.lifecycle.LiveData
import com.example.asisten_damkar.response.LoginResponse

interface LoginListener {
    fun onStarted()
    fun onNotValid()
    fun fallback(success: LiveData<LoginResponse?>)
}