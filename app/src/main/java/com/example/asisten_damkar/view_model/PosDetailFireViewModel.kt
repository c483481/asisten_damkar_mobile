package com.example.asisten_damkar.view_model

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.asisten_damkar.listener.PosDetailActiveListener
import com.example.asisten_damkar.repository.FireLocationRepository
import com.example.asisten_damkar.repository.PosRepository
import com.example.asisten_damkar.utils.LoginUtils
import com.google.android.gms.maps.model.LatLng

class PosDetailFireViewModel: ViewModel() {
    private val posRepository = PosRepository()
    private val fireLocationRepository = FireLocationRepository()
    lateinit var loginUtils: LoginUtils
    lateinit var lifecycleOwner: LifecycleOwner
    lateinit var posDetailActiveListener: PosDetailActiveListener

    fun getAllActivePos() {
        val result = posRepository.getAllActivePos(loginUtils.getAccessToken()!!)

        result.observe(lifecycleOwner, Observer {
            if(it == null) {
                posDetailActiveListener.onFailed()
                return@Observer
            }
            posDetailActiveListener.onSuccessFetch(it)
        })
    }

    fun addFireLocation(posXid: String, latLng: LatLng) {
        val data = fireLocationRepository.postFireLocation(loginUtils.getAccessToken()!!, posXid, latLng)

        data.observe(lifecycleOwner, Observer {
            if(it) {
                posDetailActiveListener.onSuccessAddFire()
                return@Observer
            }
            posDetailActiveListener.onFailed()
        })
    }
}