package com.example.asisten_damkar.view_model

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.asisten_damkar.listener.MonitoringListener
import com.example.asisten_damkar.repository.PosRepository
import com.example.asisten_damkar.utils.LoginUtils

class MonitoringViewModel: ViewModel() {
    private val posRepository = PosRepository();
    lateinit var loginUtils: LoginUtils
    lateinit var lifecycleOwner: LifecycleOwner
    lateinit var monitoringListener: MonitoringListener

    fun getAllPos() {
        val result = posRepository.getAllPos(loginUtils.getAccessToken()!!)

        result.observe(lifecycleOwner, Observer {
            if(it == null) {
                monitoringListener.onFailed()
                return@Observer
            }
            monitoringListener.onSuccessFetch(it)
        })
    }
}