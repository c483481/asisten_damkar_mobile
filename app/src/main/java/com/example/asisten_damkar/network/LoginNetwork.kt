package com.example.asisten_damkar.network

import com.example.asisten_damkar.response.LoginResponse
import com.example.asisten_damkar.response.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginNetwork {
    @FormUrlEncoded
    @POST("/auth/login")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<Response<LoginResponse>>

    companion object {
        operator fun invoke(): LoginNetwork {
            return Retrofit.Builder()
                .baseUrl("http://10.0.3.2:777")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LoginNetwork::class.java)
        }
    }
}
