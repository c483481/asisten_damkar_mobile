package com.example.asisten_damkar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.asisten_damkar.network.PosNetwork
import com.example.asisten_damkar.network.PosPostRequestBody
import com.example.asisten_damkar.response.PosResponse
import com.example.asisten_damkar.response.ResponseList
import com.google.android.gms.maps.model.LatLng
import retrofit2.Call
import com.example.asisten_damkar.response.Response

class PosRepository {
    val posNetwork: PosNetwork = PosNetwork()

    fun getListPos(token: String, latLng: LatLng): LiveData<ResponseList<PosResponse>> {
        val data = MutableLiveData<ResponseList<PosResponse>>()

        posNetwork.getPos("Bearer $token", latLng.longitude, latLng.latitude).enqueue(object: retrofit2.Callback<Response<ResponseList<PosResponse>>> {
            override fun onResponse(
                call: Call<Response<ResponseList<PosResponse>>>,
                response: retrofit2.Response<Response<ResponseList<PosResponse>>>
            ) {
                if(response.isSuccessful) {
                    data.value = response.body()!!.data!!
                    return
                }

                data.value = ResponseList(items = arrayOf(), count = 0)
            }

            override fun onFailure(call: Call<Response<ResponseList<PosResponse>>>, t: Throwable) {
                data.value = ResponseList(items = arrayOf(), count = 0)
            }

        })

        return data
    }

    fun postAddPos(token: String, latLng: LatLng, name: String): LiveData<Boolean> {
        val data = MutableLiveData<Boolean>()

        posNetwork.posAddPos(token="Bearer $token", PosPostRequestBody(name, lat = latLng.latitude, lng = latLng.longitude)).enqueue(object: retrofit2.Callback<Response<PosResponse>> {
            override fun onResponse(
                call: Call<Response<PosResponse>>,
                response: retrofit2.Response<Response<PosResponse>>
            ) {
                Log.i("repository", "onResponse: ${response.message()}")
                data.value = response.isSuccessful
            }

            override fun onFailure(call: Call<Response<PosResponse>>, t: Throwable) {
                Log.i("repository", "onResponse: ${t.message}")
                data.value = false
            }

        })

        return data
    }
}
