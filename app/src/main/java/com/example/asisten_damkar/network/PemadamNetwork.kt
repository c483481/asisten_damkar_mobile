package com.example.asisten_damkar.network

import com.example.asisten_damkar.response.PemadamResponse
import com.example.asisten_damkar.response.Response
import com.example.asisten_damkar.utils.getBaseUrl
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface PemadamNetwork {
    @GET("pemadam/{xid}")
    fun getInfo(@Header("Authorization") token: String, @Path("xid") xid: String): Call<Response<PemadamResponse>>
    companion object {
        operator fun invoke(): PemadamNetwork {
            return Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PemadamNetwork::class.java)
        }
    }
}