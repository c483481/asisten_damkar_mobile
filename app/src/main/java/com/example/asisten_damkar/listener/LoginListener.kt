package com.example.asisten_damkar.listener

import androidx.lifecycle.LiveData

interface LoginListener {
    fun onStarted()
    fun onNotValid(message: String)

    fun onDone(message: LiveData<String>)
}