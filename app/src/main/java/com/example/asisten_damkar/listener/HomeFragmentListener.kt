package com.example.asisten_damkar.listener

import com.example.asisten_damkar.response.FireLocationResponse

interface HomeFragmentListener {
    fun handleTitikKebakaran()

    fun handlePosTracker()

    fun onCallbackResponse(data: Array<FireLocationResponse>)
}