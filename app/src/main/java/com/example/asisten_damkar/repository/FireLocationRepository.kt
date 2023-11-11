package com.example.asisten_damkar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.asisten_damkar.network.FireLocationNetwork
import com.example.asisten_damkar.network.FireLocationPostRequestBody
import com.example.asisten_damkar.response.FireLocationResponse
import com.example.asisten_damkar.response.Response
import com.example.asisten_damkar.response.ResponseList
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

    fun getLocation(token: String) : LiveData<Array<FireLocationResponse>> {
        val data = MutableLiveData<Array<FireLocationResponse>>()

        fireLocationNetwork.getFireLocationHomeFragment("Bearer $token").enqueue(object : Callback<Response<ResponseList<FireLocationResponse>>> {
            override fun onResponse(
                call: Call<Response<ResponseList<FireLocationResponse>>>,
                response: retrofit2.Response<Response<ResponseList<FireLocationResponse>>>
            ) {
                if(response.isSuccessful) {
                    Log.i("data berhasil", response.body()!!.toString())
                    data.value = response.body()!!.data.items
                    return
                }
                data.value = arrayOf()
            }

            override fun onFailure(call: Call<Response<ResponseList<FireLocationResponse>>>, t: Throwable) {
                data.value = arrayOf()
            }

        })

        return data
    }

    fun getActiveFireLocation(token: String) : LiveData<Array<FireLocationResponse>?> {
        val data = MutableLiveData<Array<FireLocationResponse>?>()

        fireLocationNetwork.getFireLocationHomeFragment("Bearer $token", showAll = true).enqueue(object: Callback<Response<ResponseList<FireLocationResponse>>> {
            override fun onResponse(
                call: Call<Response<ResponseList<FireLocationResponse>>>,
                response: retrofit2.Response<Response<ResponseList<FireLocationResponse>>>
            ) {
                if(response.isSuccessful) {
                    data.value = response.body()!!.data.items
                    return
                }

                data.value = null
            }

            override fun onFailure(
                call: Call<Response<ResponseList<FireLocationResponse>>>,
                t: Throwable
            ) {
                data.value = null
            }

        })

        return data
    }

    fun getActiveFireArriveLocation(token: String) : LiveData<Array<FireLocationResponse>?> {
        val data = MutableLiveData<Array<FireLocationResponse>?>()

        fireLocationNetwork.getFireLocationHomeFragment("Bearer $token", showAll = true, arriveAt = true, status = null).enqueue(object: Callback<Response<ResponseList<FireLocationResponse>>> {
            override fun onResponse(
                call: Call<Response<ResponseList<FireLocationResponse>>>,
                response: retrofit2.Response<Response<ResponseList<FireLocationResponse>>>
            ) {
                if(response.isSuccessful) {
                    data.value = response.body()!!.data.items
                    return
                }

                data.value = null
            }

            override fun onFailure(
                call: Call<Response<ResponseList<FireLocationResponse>>>,
                t: Throwable
            ) {
                data.value = null
            }

        })

        return data
    }
}