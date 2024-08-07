package com.example.asisten_damkar.network

import com.example.asisten_damkar.response.PosJoinResponse
import com.example.asisten_damkar.response.PosResponse
import com.example.asisten_damkar.response.Response
import com.example.asisten_damkar.response.ResponseList
import com.example.asisten_damkar.utils.getBaseUrl
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PosNetwork {
    @GET("pos")
    fun getPos(
        @Header("Authorization") token: String,
        @Query("filters[lng]") lng: Double,
        @Query("filters[lat]") lat: Double,
        @Query("filters[km]") km: Int = 10,
        @Query("showAll") showAll: Boolean = true,
    ): Call<Response<ResponseList<PosResponse>>>

    @GET("pos")
    fun getAllPos(
        @Header("Authorization") token: String,
    ): Call<Response<ResponseList<PosResponse>>>

    @GET("pos")
    fun getAllActivePos(
        @Header("Authorization") token: String,
        @Query("showAll") showAll: Boolean = true,
        @Query("filters[includeActive]") active: Boolean = true
    ): Call<Response<ResponseList<PosResponse>>>

    @Headers("Content-Type: application/json")
    @POST("pos")
    fun posAddPos(
        @Header("Authorization") token: String,
        @Body data: PosPostRequestBody
    ): Call<Response<PosResponse>>

    @GET("pos/{xid}")
    fun getDetailPos(
        @Header("Authorization") token: String,
        @Path("xid") xid: String
    ): Call<Response<PosJoinResponse>>

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

data class PosPostRequestBody(
    val name: String,
    val lat: Double,
    val lng: Double,
)