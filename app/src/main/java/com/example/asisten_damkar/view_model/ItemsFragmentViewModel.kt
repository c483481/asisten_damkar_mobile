package com.example.asisten_damkar.view_model

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.asisten_damkar.listener.AddItemsListener
import com.example.asisten_damkar.repository.TruckRepository
import com.example.asisten_damkar.utils.LoginUtils

class ItemsFragmentViewModel: ViewModel() {
    lateinit var addItemsListener: AddItemsListener
    lateinit var lifecycle: LifecycleOwner
    lateinit var loginUtils: LoginUtils
    private val truckRepository = TruckRepository()

    fun addItems(name: String?) {
        if(name.isNullOrEmpty()) {
            addItemsListener.onNotValid()
            return
        }

        val result = truckRepository.addItems(loginUtils.getAccessToken()!!, loginUtils.getTruckXid()!!, name)

        result.observe(lifecycle, Observer {
            addItemsListener.onSubmitted(it)
        })
    }
}