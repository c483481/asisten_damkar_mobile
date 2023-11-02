package com.example.asisten_damkar.view_model

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.asisten_damkar.listener.AddPosListener
import com.example.asisten_damkar.repository.PosRepository
import com.example.asisten_damkar.utils.LoginUtils
import com.google.android.gms.maps.model.LatLng

class AddPosModel: ViewModel() {
    lateinit var loginUtils: LoginUtils
    lateinit var addPosListener: AddPosListener
    val posRepository: PosRepository = PosRepository()

    fun onSubmitted(name: String, latLng: LatLng) {
        if(name.isNullOrEmpty()) {
            addPosListener!!.onNotValid()
            return
        }
        val response = posRepository.postAddPos(loginUtils!!.getAccessToken()!! ,latLng, name)
        addPosListener!!.onSubmitted(response)
    }
}