package com.example.asisten_damkar.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asisten_damkar.R
import com.example.asisten_damkar.adapter.PosListAdapter
import com.example.asisten_damkar.databinding.ActivityPosDetailFireBinding
import com.example.asisten_damkar.listener.OnClickAdapter
import com.example.asisten_damkar.listener.PosDetailActiveListener
import com.example.asisten_damkar.response.PosResponse
import com.example.asisten_damkar.response.ResponseList
import com.example.asisten_damkar.utils.LoginUtils
import com.example.asisten_damkar.utils.hide
import com.example.asisten_damkar.utils.show
import com.example.asisten_damkar.view_model.PosDetailFireViewModel
import com.google.android.gms.maps.model.LatLng

class PosDetailFireActivity : AppCompatActivity(), PosDetailActiveListener {
    lateinit var binding: ActivityPosDetailFireBinding
    lateinit var latLng: LatLng
    lateinit var model: PosDetailFireViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pos_detail_fire)
        val lat = intent.getDoubleExtra("lat", 0.0)
        val lng = intent.getDoubleExtra("lng", 0.0)
        latLng = LatLng(lat, lng)
        model = ViewModelProvider(this)[PosDetailFireViewModel::class.java]

        model.posDetailActiveListener = this
        model.lifecycleOwner = this
        model.loginUtils = LoginUtils(this)


        binding.progressBarPos.show()
        model.getAllActivePos()
    }

    override fun onSuccessFetch(data: ResponseList<PosResponse>) {
        binding.progressBarPos.hide()
        binding.scrollViewPos.visibility = View.VISIBLE

        val listener = object : OnClickAdapter<PosResponse> {
            override fun onClickAdapter(data: PosResponse) {
                binding.progressBarPos.show()
                model.addFireLocation(data.xid, latLng)
            }
        }

        val adapter = PosListAdapter(data.items, listener)
        val layoutManager = LinearLayoutManager(this)
        binding.posDetailRecyclerView.layoutManager = layoutManager
        binding.posDetailRecyclerView.adapter = adapter
    }

    override fun onFailed() {
        binding.progressBarPos.hide()
        Toast.makeText(this, "Failed to load data, please login again", Toast.LENGTH_LONG).show()
    }

    override fun onSuccessAddFire() {
        binding.progressBarPos.hide()
        val i = Intent(this, SuccessPageActivity::class.java)
        startActivity(i)
        finish()
    }
}