package com.example.asisten_damkar.listener

import androidx.lifecycle.LiveData

interface LoginListener {
    fun onStarted()
    fun onNotValid()
    fun fallback(success: LiveData<String?>)
}