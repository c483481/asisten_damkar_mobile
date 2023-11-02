package com.example.asisten_damkar.listener

import androidx.lifecycle.LiveData

interface AddPosListener {
    fun onNotValid()
    fun onSubmitted(data: LiveData<Boolean>)
}