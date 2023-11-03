package com.example.asisten_damkar.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asisten_damkar.R
import com.example.asisten_damkar.adapter.TruckListAdapter
import com.example.asisten_damkar.databinding.ActivityPosDetailBinding
import com.example.asisten_damkar.listener.OnClickAdapter
import com.example.asisten_damkar.listener.PosDetailListener
import com.example.asisten_damkar.response.Truck
import com.example.asisten_damkar.utils.LoginUtils
import com.example.asisten_damkar.utils.hide
import com.example.asisten_damkar.utils.show
import com.example.asisten_damkar.utils.toast
import com.example.asisten_damkar.view_model.PosDetailViewModel

class PosDetailActivity : AppCompatActivity(), PosDetailListener {
    lateinit var binding: ActivityPosDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityPosDetailBinding>(this, R.layout.activity_pos_detail)

        val xid = intent.getStringExtra("posXid")!!

        binding.addTruck.setOnClickListener{
            val i = Intent(this, AddTruckActivity::class.java)
            i.putExtra("posXid", xid)
            startActivity(i)
            finish()
        }

        val viewModel = ViewModelProvider(this)[PosDetailViewModel::class.java]
        viewModel.posDetailListener = this
        viewModel.lifecycleOwner = this
        viewModel.loginUtils = LoginUtils(this)
        binding.progressBarPosDetail.show()
        viewModel.fetchPosDetail(xid)
    }

    override fun onGetResponse(items: List<Truck>) {
        binding.progressBarPosDetail.hide()
        val contex = baseContext
        val listener = object : OnClickAdapter<Truck> {
            override fun onClickAdapter(data: Truck) {
                val i = Intent(contex, TruckStatusActivity::class.java)
                i.putExtra("truckXid", data.xid)
                startActivity(i)
                finish()
            }
        }

        val adapter = TruckListAdapter(items, listener)
        val layoutManager = LinearLayoutManager(this)
        binding.posDetailRecyclerView.layoutManager = layoutManager
        binding.posDetailRecyclerView.adapter = adapter
    }

    override fun onFailedResponse() {
        binding.progressBarPosDetail.hide()
        toast("Failed to fetch, please logout and login again")
    }
}