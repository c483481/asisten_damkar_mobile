package com.example.asisten_damkar.view_model

import androidx.lifecycle.ViewModel
import com.example.asisten_damkar.listener.HomePemadamFragmentListener
import com.example.asisten_damkar.listener.HomePemadamSocketListener
import com.example.asisten_damkar.repository.FireLocationRepository
import com.example.asisten_damkar.utils.SocketPemadamUtils

class HomePemadamViewModel: ViewModel() {
    private val fireLocationRepository = FireLocationRepository()
    lateinit var homePemadamFragmentListener: HomePemadamFragmentListener
    lateinit var socketPemadamUtils: SocketPemadamUtils
    lateinit var homePemadamSocketListener: HomePemadamSocketListener

    fun fetchFireLocation(token: String, posXid: String) {
        val data = fireLocationRepository.getListLocationPemadam(token, posXid)

        homePemadamFragmentListener.onCallbackFireList(data)
    }

    fun createSocketConnection(posXid: String) {
        socketPemadamUtils.socketEvent(homePemadamSocketListener)
        socketPemadamUtils.connect(posXid)
    }
}