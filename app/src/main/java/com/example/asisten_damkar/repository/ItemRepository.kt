package com.example.asisten_damkar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.asisten_damkar.network.ItemsNetwork
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItemRepository {
    private val itemsNetwork = ItemsNetwork()

    fun updateItems(token: String, xid: String): LiveData<Boolean> {
        val data = MutableLiveData<Boolean>()

        itemsNetwork.updateData("Bearer $token", xid).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                data.value = response.isSuccessful
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                data.value = false
            }

        })

        return data
    }

    fun deleteItems(token: String, xid: String): LiveData<Boolean> {
        val data = MutableLiveData<Boolean>()

        itemsNetwork.deleteData("Bearer $token", xid).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                data.value = response.isSuccessful
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                data.value = false
            }

        })

        return data
    }
}