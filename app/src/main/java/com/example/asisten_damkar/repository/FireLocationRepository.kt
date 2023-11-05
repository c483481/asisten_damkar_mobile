package com.example.asisten_damkar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.asisten_damkar.network.FireLocationNetwork
import com.example.asisten_damkar.network.FireLocationPostRequestBody
import com.example.asisten_damkar.response.FireLocationResponse
import com.example.asisten_damkar.response.Response
import com.google.android.gms.maps.model.LatLng
import retrofit2.Call
import retrofit2.Callback

class FireLocationRepository {
    private val fireLocationNetwork = FireLocationNetwork()

    fun postFireLocation(token: String, xid: String, latLng: LatLng): LiveData<Boolean> {
        val data = MutableLiveData<Boolean>()

        fireLocationNetwork.posAddPos("Bearer $token", FireLocationPostRequestBody(posXid = xid, lat = latLng.latitude, lng = latLng.longitude)).enqueue(object : Callback<Response<FireLocationResponse>> {
            override fun onResponse(
                call: Call<Response<FireLocationResponse>>,
                response: retrofit2.Response<Response<FireLocationResponse>>
            ) {
                data.value = response.isSuccessful
            }

            override fun onFailure(call: Call<Response<FireLocationResponse>>, t: Throwable) {
                data.value = false
            }
        })

        return data
    }
}