package com.example.asisten_damkar.view_model

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.asisten_damkar.listener.ActivityFragmentListener
import com.example.asisten_damkar.repository.FireLocationRepository

class ActivityFragmentViewModel: ViewModel() {
    private val fireLocationRepository = FireLocationRepository()
    lateinit var lifecycleOwner: LifecycleOwner
    lateinit var activityFragmentListener: ActivityFragmentListener

    fun loadData(token: String, isActive: Boolean) {
        if(isActive) {
            val data = fireLocationRepository.getActiveFireLocation(token)

            data.observe(lifecycleOwner, Observer {
                if(it == null){
                    activityFragmentListener.onFailed()
                    return@Observer
                }
                activityFragmentListener.onSuccess(it)
            })
        } else {
            val data = fireLocationRepository.getActiveFireArriveLocation(token)

            data.observe(lifecycleOwner, Observer {
                if(it == null){
                    activityFragmentListener.onFailed()
                    return@Observer
                }
                activityFragmentListener.onSuccess(it)
            })
        }
    }
}