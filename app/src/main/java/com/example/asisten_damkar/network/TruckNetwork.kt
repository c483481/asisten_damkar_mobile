package com.example.asisten_damkar.network

import com.example.asisten_damkar.response.Response
import com.example.asisten_damkar.response.TruckDetailResponse
import com.example.asisten_damkar.response.TruckResponse
import com.example.asisten_damkar.utils.getBaseUrl
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface TruckNetwork {
    @Headers("Content-Type: application/json")
    @POST("truck")
    fun posAddPos(
        @Header("Authorization") token: String,
        @Body data: TruckRequestBody
    ): Call<Response<TruckResponse>>

    @GET("truck/{xid}")
    fun getDetailPos(
        @Header("Authorization") token: String,
        @Path("xid") xid: String
    ): Call<Response<TruckDetailResponse>>

    companion object {
        operator fun invoke(): TruckNetwork {
            return Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TruckNetwork::class.java)
        }
    }
}

data class TruckRequestBody(
    val plat: String,
    val posXid: String,
)