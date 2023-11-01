package com.example.asisten_damkar.view_model

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.asisten_damkar.listener.HomeFragmentListener

class HomeViewModel: ViewModel() {
    var homeListener: HomeFragmentListener? = null

    fun onTitikKebakaranClick(view: View) {
        homeListener!!.handleTitikKebakaran()
    }

    fun onTrackingPosClick(view: View) {
        homeListener!!.handlePosTracker()
    }
}