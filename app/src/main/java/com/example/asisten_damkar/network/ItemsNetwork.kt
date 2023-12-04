package com.example.asisten_damkar.network

import com.example.asisten_damkar.utils.getBaseUrl
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path

interface ItemsNetwork {

    @PATCH("/items/{xid}")
    fun updateData(
        @Header("Authorization") token: String,
        @Path("xid") xid: String
    ): Call<Any>

    @DELETE("/items/{xid}")
    fun deleteData(
        @Header("Authorization") token: String,
        @Path("xid") xid: String
    ): Call<Any>

    companion object {
        operator fun invoke(): ItemsNetwork {
            return Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ItemsNetwork::class.java)
        }
    }
}