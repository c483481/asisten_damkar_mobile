package com.example.asisten_damkar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.asisten_damkar.network.PemadamNetwork
import com.example.asisten_damkar.response.PemadamResponse
import com.example.asisten_damkar.response.Response
import retrofit2.Call
import retrofit2.Callback

class PemadamRepository {
    private val pemadamNetwork = PemadamNetwork()

    fun getPemadamInfo(token: String, xid: String): LiveData<PemadamResponse?> {
        val data = MutableLiveData<PemadamResponse?>()

        pemadamNetwork.getInfo("Bearer $token", xid).enqueue(object : Callback<Response<PemadamResponse>> {
            override fun onResponse(
                call: Call<Response<PemadamResponse>>,
                response: retrofit2.Response<Response<PemadamResponse>>
            ) {
                if(response.isSuccessful) {
                    data.value = response.body()!!.data
                    return
                }
                data.value = null
            }

            override fun onFailure(call: Call<Response<PemadamResponse>>, t: Throwable) {
                data.value = null
            }

        })

        return data
    }
}