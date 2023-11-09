package com.example.asisten_damkar.view_model

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.asisten_damkar.listener.HomeFragmentListener
import com.example.asisten_damkar.repository.FireLocationRepository

class HomeViewModel: ViewModel() {
    var homeListener: HomeFragmentListener? = null
    private val fireLocationRepository = FireLocationRepository()
    lateinit var lifecycleOwner: LifecycleOwner

    fun fetchFirstData(token: String) {
        val data = fireLocationRepository.getLocation(token)

        data.observe(lifecycleOwner, Observer {
            homeListener!!.onCallbackResponse(it)
        })
    }

    fun onTitikKebakaranClick(view: View) {
        homeListener!!.handleTitikKebakaran()
    }

    fun onTrackingPosClick(view: View) {
        homeListener!!.handlePosTracker()
    }
}