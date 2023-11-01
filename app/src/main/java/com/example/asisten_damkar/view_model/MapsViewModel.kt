package com.example.asisten_damkar.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.asisten_damkar.repository.PosRepository
import com.example.asisten_damkar.response.PosResponse
import com.example.asisten_damkar.response.ResponseList
import com.google.android.gms.maps.model.LatLng

class MapsViewModel: ViewModel() {
    private val posRepository: PosRepository = PosRepository()

    fun getPosList(token: String, lng: LatLng): LiveData<ResponseList<PosResponse>> {
        return posRepository.getListPos(token, lng)
    }
}