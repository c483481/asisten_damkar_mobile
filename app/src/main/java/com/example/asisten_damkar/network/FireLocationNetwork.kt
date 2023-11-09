package com.example.asisten_damkar.network

import com.example.asisten_damkar.response.FireLocationResponse
import com.example.asisten_damkar.response.Response
import com.example.asisten_damkar.response.ResponseList
import com.example.asisten_damkar.utils.getBaseUrl
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface FireLocationNetwork {
    @Headers("Content-Type: application/json")
    @POST("fire-location")
    fun posAddPos(
        @Header("Authorization") token: String,
        @Body payload: FireLocationPostRequestBody
    ): Call<Response<FireLocationResponse>>

    @GET("fire-location")
    fun getFireLocationHomeFragment(
        @Header("Authorization") token: String,
        @Query("status") status: Int = 1,
        @Query("limit") limit: Int = 3
    ): Call<Response<ResponseList<FireLocationResponse>>>

    companion object {
        operator fun invoke(): FireLocationNetwork {
            return Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FireLocationNetwork::class.java)
        }
    }
}

data class FireLocationPostRequestBody(
    val posXid: String,
    val lat: Double,
    val lng: Double,
)