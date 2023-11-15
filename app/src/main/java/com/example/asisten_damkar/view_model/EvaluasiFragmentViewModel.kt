package com.example.asisten_damkar.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.asisten_damkar.repository.FireLocationRepository
import com.example.asisten_damkar.repository.PosRepository
import com.example.asisten_damkar.response.FireLocationResponse
import com.example.asisten_damkar.response.PosResponse
import com.example.asisten_damkar.response.ResponseList

class EvaluasiFragmentViewModel: ViewModel() {
    private val fireLocationRepository = FireLocationRepository()
    private val posRepository = PosRepository()

    fun fetchFirstData(token: String, posXid: String?): LiveData<ResponseList<FireLocationResponse>?> {
        return fireLocationRepository.getAllFireLocationResult(token, posXid)
    }

    fun fetchPosData(token: String): LiveData<ResponseList<PosResponse>> {
        return posRepository.getAllPos(token)
    }
}