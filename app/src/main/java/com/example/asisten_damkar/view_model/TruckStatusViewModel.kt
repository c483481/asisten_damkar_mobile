package com.example.asisten_damkar.view_model

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.asisten_damkar.listener.AddTruckListener
import com.example.asisten_damkar.listener.TruckStatusListener
import com.example.asisten_damkar.repository.ItemRepository
import com.example.asisten_damkar.repository.TruckRepository
import com.example.asisten_damkar.utils.LoginUtils

class TruckStatusViewModel: ViewModel() {
    private val truckRepository = TruckRepository()
    lateinit var loginUtils: LoginUtils
    lateinit var truckStatusListener: TruckStatusListener
    lateinit var lifecycleOwner: LifecycleOwner
    private val itemRepository = ItemRepository()

    fun getDetailTruck(xid: String) {
        val result = truckRepository.getDetailTruck(loginUtils.getAccessToken()!!, xid)

        result.observe(lifecycleOwner, Observer {
            if(it == null) {
                truckStatusListener.onFailed()
                return@Observer
            }

            truckStatusListener.onSuccess(it)
        })
    }
    fun updateItem(xid: String) {
        val result = itemRepository.updateItems(loginUtils.getAccessToken()!!, xid)
        truckStatusListener.loading()
        result.observeForever {
            truckStatusListener.clear()
            truckStatusListener.update(it, xid)
        }
    }
    fun deleteItem(xid: String) {
        val result = itemRepository.deleteItems(loginUtils.getAccessToken()!!, xid)
        truckStatusListener.loading()
        result.observeForever {
            truckStatusListener.clear()
            truckStatusListener.delete(it, xid)
        }
    }
}