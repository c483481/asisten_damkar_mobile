package com.example.asisten_damkar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.asisten_damkar.network.TruckNetwork
import com.example.asisten_damkar.network.TruckRequestBody
import com.example.asisten_damkar.response.Response
import com.example.asisten_damkar.response.TruckResponse
import retrofit2.Call
import retrofit2.Callback

class TruckRepository {
    private val truckNetwork: TruckNetwork = TruckNetwork()

    fun addTruck(token: String, posXid: String, plat: String): LiveData<Boolean> {
        val data = MutableLiveData<Boolean>()

        truckNetwork.posAddPos("Bearer $token", TruckRequestBody(plat, posXid)).enqueue(object: Callback<Response<TruckResponse>> {
            override fun onResponse(
                call: Call<Response<TruckResponse>>,
                response: retrofit2.Response<Response<TruckResponse>>
            ) {
                data.value = response.isSuccessful
            }

            override fun onFailure(call: Call<Response<TruckResponse>>, t: Throwable) {
                data.value = false
            }

        })

        return data
    }
}