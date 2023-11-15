package com.example.asisten_damkar.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.asisten_damkar.repository.FireLocationRepository
import com.example.asisten_damkar.response.FireLocationResponse
import com.example.asisten_damkar.response.ResponseList

class EvaluasiFragmentViewModel: ViewModel() {
    private val fireLocationRepository = FireLocationRepository()

    fun fetchFirstData(token: String, posXid: String?): LiveData<ResponseList<FireLocationResponse>?> {
        return fireLocationRepository.getAllFireLocationResult(token, posXid)
    }
}