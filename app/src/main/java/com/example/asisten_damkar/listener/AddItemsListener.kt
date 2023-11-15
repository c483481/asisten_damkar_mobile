package com.example.asisten_damkar.listener

import androidx.lifecycle.LiveData

interface AddItemsListener {
    fun onNotValid()
    fun onSubmitted(data: Boolean)
}