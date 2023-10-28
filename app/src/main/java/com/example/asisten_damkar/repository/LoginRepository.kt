package com.example.asisten_damkar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.asisten_damkar.network.LoginNetwork
import com.example.asisten_damkar.response.LoginResponse
import com.example.asisten_damkar.response.Response
import retrofit2.Call

class LoginRepository() {
    fun login(username: String, password: String): LiveData<String> {
        Log.i("login repository", "login: ini di panggil")
        val loginResponse = MutableLiveData<String>()
        LoginNetwork().login(username=username, password=password).enqueue(object : retrofit2.Callback<Response<LoginResponse>> {
            override fun onFailure(call: Call<Response<LoginResponse>>, t: Throwable) {
                loginResponse.value = t.message
            }

            override fun onResponse(
                call: Call<Response<LoginResponse>>,
                response: retrofit2.Response<Response<LoginResponse>>
            ) {
                if(response.isSuccessful) {
                    loginResponse.value = response.body()?.toString()
                    return
                }
                loginResponse.value = response.errorBody()?.toString()
            }
        })

        return loginResponse
    }
}