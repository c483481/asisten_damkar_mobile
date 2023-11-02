package com.example.asisten_damkar.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.asisten_damkar.R
import com.example.asisten_damkar.databinding.ActivityAddPosBinding
import com.example.asisten_damkar.listener.AddPosListener
import com.example.asisten_damkar.utils.LoginUtils
import com.example.asisten_damkar.utils.hide
import com.example.asisten_damkar.utils.show
import com.example.asisten_damkar.utils.toast
import com.example.asisten_damkar.view_model.AddPosModel
import com.example.asisten_damkar.view_model.LoginViewModel
import com.google.android.gms.maps.model.LatLng

class AddPosActivity : AppCompatActivity(), AddPosListener {
    lateinit var binding: ActivityAddPosBinding
    lateinit var latLng: LatLng
    lateinit var viewModel: AddPosModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_pos)

        if(!intent.hasExtra("lat") || !intent.hasExtra("lng")) {
            val i = Intent(this, HomeFragment::class.java)
            startActivity(i)
            finish()
        }
        latLng = LatLng(intent.getDoubleExtra("lat", 0.0), intent.getDoubleExtra("lng", 0.0))

        viewModel = ViewModelProvider(this)[AddPosModel::class.java]
        binding.model = viewModel
        viewModel.addPosListener = this
        viewModel.loginUtils = LoginUtils(this)

        binding.submitted.setOnClickListener {
            binding.progressBarPos.show()
            viewModel.onSubmitted(binding.posName.text.toString(), latLng)
        }
    }

    override fun onNotValid() {
        binding.progressBarPos.hide()
        toast("please enter the name")
    }

    override fun onSubmitted(data: LiveData<Boolean>) {
        data.observe(this, Observer {
            binding.progressBarPos.hide()
            if(it) {
                toast("berhasil menambahkan pos")
                val i = Intent(this, HomeActivity::class.java)
                startActivity(i)
                return@Observer
            }
            toast("Gagal menambahkan pos")
        })
    }
}