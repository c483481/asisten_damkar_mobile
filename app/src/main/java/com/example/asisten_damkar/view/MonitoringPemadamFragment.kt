package com.example.asisten_damkar.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asisten_damkar.adapter.ItemsListAdapter
import com.example.asisten_damkar.databinding.FragmentMonitoringPemadamBinding
import com.example.asisten_damkar.listener.TruckStatusListener
import com.example.asisten_damkar.response.TruckDetailResponse
import com.example.asisten_damkar.utils.LoginUtils
import com.example.asisten_damkar.utils.getStringDate
import com.example.asisten_damkar.utils.hide
import com.example.asisten_damkar.utils.show
import com.example.asisten_damkar.view_model.TruckStatusViewModel

class MonitoringPemadamFragment : Fragment(), TruckStatusListener {
    lateinit var binding: FragmentMonitoringPemadamBinding
    lateinit var loginUtils: LoginUtils
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMonitoringPemadamBinding.inflate(inflater)

        loginUtils = LoginUtils(container!!.context)
        val xid = loginUtils.getTruckXid()!!
        val model = ViewModelProvider(this)[TruckStatusViewModel::class.java]
        model.truckStatusListener = this
        model.lifecycleOwner = this
        model.loginUtils = loginUtils

        binding.addItems.setOnClickListener {
            val i = Intent(context, AddItemsActivity::class.java)
            startActivity(i)
        }

        binding.progressBarTruck.show()
        model.getDetailTruck(xid)


        return binding.root
    }

    override fun onSuccess(data: TruckDetailResponse) {
        binding.progressBarTruck.hide()
        binding.scrollViewTruckStatus.visibility = View.VISIBLE

        binding.totalItemsTruck.text = data.items.count().toString()
        binding.platTruck.text = data.plat
        binding.kondisiTruck.text = if(data.active) "Bisa Digunakan" else "Tidak Active"
        binding.pengecekanTruck.text = getStringDate(data.updatedAt)

        val adapter = ItemsListAdapter(data.items)
        val layoutManager = LinearLayoutManager(context)
        binding.recyclerViewItems.layoutManager = layoutManager
        binding.recyclerViewItems.adapter = adapter
    }

    override fun onFailed() {
        Toast.makeText(context, "ada yang salah, silahkan login kembali...", Toast.LENGTH_LONG).show()
    }
}