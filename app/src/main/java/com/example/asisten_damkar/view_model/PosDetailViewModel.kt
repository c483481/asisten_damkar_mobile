package com.example.asisten_damkar.view_model

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.asisten_damkar.listener.PosDetailListener
import com.example.asisten_damkar.repository.PosRepository
import com.example.asisten_damkar.utils.LoginUtils

class PosDetailViewModel: ViewModel() {
    lateinit var posDetailListener: PosDetailListener
    lateinit var loginUtils: LoginUtils
    private val posRepository = PosRepository()
    lateinit var lifecycleOwner: LifecycleOwner

    fun fetchPosDetail(xid: String) {
        val data = posRepository.getDetailPos(loginUtils.getAccessToken()!!, xid)
        data.observe(lifecycleOwner, Observer {
            if(it == null) {
                posDetailListener.onFailedResponse()
                return@Observer
            }
            posDetailListener.onGetResponse(it)
        })
    }
}