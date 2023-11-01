package com.example.asisten_damkar.network

import com.example.asisten_damkar.response.PosResponse
import com.example.asisten_damkar.response.Response
import com.example.asisten_damkar.response.ResponseList
import com.example.asisten_damkar.utils.getBaseUrl
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PosNetwork {
    @GET("pos")
    fun getPos(
        @Header("Authorization") token: String,
        @Query("lng") lng: Double,
        @Query("lat") lat: Double,
        @Query("filters[km]") km: Int = 1,
        @Query("showAll") showAll: Boolean = true,
    ): Call<Response<ResponseList<PosResponse>>>

    companion object {
        operator fun invoke(): PosNetwork {
            return Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PosNetwork::class.java)
        }
    }
}