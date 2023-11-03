package com.example.asisten_damkar.view_model

import androidx.lifecycle.ViewModel
import com.example.asisten_damkar.listener.AddTruckListener
import com.example.asisten_damkar.repository.TruckRepository
import com.example.asisten_damkar.utils.LoginUtils

class AddTruckViewModel: ViewModel() {
    private val truckRepository: TruckRepository = TruckRepository()
    lateinit var loginUtils: LoginUtils
    lateinit var addTruckListener: AddTruckListener

    fun addTruck(xid: String, plat: String?) {
        if(plat.isNullOrEmpty() || plat.length < 5 || plat.length > 12) {
            addTruckListener.onInvalidPlat()
            return
        }

        val result = truckRepository.addTruck(loginUtils.getAccessToken()!!, xid, plat)
        addTruckListener.onCallbackResponse(result)
    }
}