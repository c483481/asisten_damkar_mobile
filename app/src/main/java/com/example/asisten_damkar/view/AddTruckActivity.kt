package com.example.asisten_damkar.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.asisten_damkar.R
import com.example.asisten_damkar.databinding.ActivityAddTruckBinding
import com.example.asisten_damkar.listener.AddTruckListener
import com.example.asisten_damkar.utils.LoginUtils
import com.example.asisten_damkar.utils.hide
import com.example.asisten_damkar.utils.show
import com.example.asisten_damkar.utils.toast
import com.example.asisten_damkar.view_model.AddTruckViewModel

class AddTruckActivity : AppCompatActivity(), AddTruckListener {
    lateinit var binding: ActivityAddTruckBinding;
    lateinit var posXid: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_truck)
        val model = ViewModelProvider(this)[AddTruckViewModel::class.java]
        model.addTruckListener = this
        model.loginUtils = LoginUtils(this)

        posXid = intent.getStringExtra("posXid")!!

        binding.submitted.setOnClickListener{
            binding.progressBarTruck.show()
            model.addTruck(posXid, binding.truckPlat.text.toString())
        }
    }

    override fun onCallbackResponse(data: LiveData<Boolean>) {
        data.observe(this, Observer {
            binding.progressBarTruck.hide()
            if(!it) {
                toast("Gagal menambahkan Truck")
                return@Observer
            }
            toast("Berhasil menambahkan truck")
            val i = Intent(this, PosDetailActivity::class.java)
            i.putExtra("posXid", posXid)
            startActivity(i)
        })

    }

    override fun onInvalidPlat() {
        binding.progressBarTruck.hide()
        toast("Masukan PLat yang benar")
    }
}