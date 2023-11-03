package com.example.asisten_damkar.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asisten_damkar.R
import com.example.asisten_damkar.adapter.ItemsListAdapter
import com.example.asisten_damkar.databinding.ActivityTruckStatusBinding
import com.example.asisten_damkar.listener.TruckStatusListener
import com.example.asisten_damkar.response.TruckDetailResponse
import com.example.asisten_damkar.utils.LoginUtils
import com.example.asisten_damkar.utils.getStringDate
import com.example.asisten_damkar.utils.hide
import com.example.asisten_damkar.utils.show
import com.example.asisten_damkar.utils.toast
import com.example.asisten_damkar.view_model.TruckStatusViewModel

class TruckStatusActivity : AppCompatActivity(), TruckStatusListener {

    lateinit var binding: ActivityTruckStatusBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_truck_status)

        val xid = intent.getStringExtra("truckXid")!!
        val model = ViewModelProvider(this)[TruckStatusViewModel::class.java]
        model.truckStatusListener = this
        model.lifecycleOwner = this
        model.loginUtils = LoginUtils(this)

        binding.progressBarTruck.show()
        model.getDetailTruck(xid)
    }

    override fun onSuccess(data: TruckDetailResponse) {
        binding.progressBarTruck.hide()
        binding.scrollViewTruckStatus.visibility = View.VISIBLE

        binding.totalItemsTruck.text = data.items.count().toString()
        binding.platTruck.text = data.plat
        binding.kondisiTruck.text = if(data.active) "Bisa Digunakan" else "Tidak Active"
        binding.pengecekanTruck.text = getStringDate(data.updatedAt)

        val adapter = ItemsListAdapter(data.items)
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerViewItems.layoutManager = layoutManager
        binding.recyclerViewItems.adapter = adapter
    }

    override fun onFailed() {
        toast("ada yang salah, silahkan login kembali...")
    }
}