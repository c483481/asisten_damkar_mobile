package com.example.asisten_damkar.listener

import com.example.asisten_damkar.response.FireLocationResponse

interface ActivityFragmentListener {
    fun onSuccess(items: Array<FireLocationResponse>)

    fun onFailed()
}
