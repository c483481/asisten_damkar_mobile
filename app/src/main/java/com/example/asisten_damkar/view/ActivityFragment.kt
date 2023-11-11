package com.example.asisten_damkar.view

import android.R
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asisten_damkar.adapter.AdapterFireLocationList
import com.example.asisten_damkar.databinding.FragmentActivityBinding
import com.example.asisten_damkar.listener.ActivityFragmentListener
import com.example.asisten_damkar.response.FireLocationResponse
import com.example.asisten_damkar.utils.LoginUtils
import com.example.asisten_damkar.utils.hide
import com.example.asisten_damkar.utils.show
import com.example.asisten_damkar.view_model.ActivityFragmentViewModel
import java.util.Locale


class ActivityFragment : Fragment(), ActivityFragmentListener {
    lateinit var binding: FragmentActivityBinding
    lateinit var model: ActivityFragmentViewModel
    lateinit var loginUtils: LoginUtils
    lateinit var geocoder: Geocoder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentActivityBinding.inflate(inflater)

        geocoder = Geocoder(container!!.context, Locale.getDefault())

        loginUtils = LoginUtils(container!!.context)

        binding.progressBarActivity.show()

        model = ViewModelProvider(this)[ActivityFragmentViewModel::class.java]

        model.activityFragmentListener = this

        model.lifecycleOwner = this

        model.loadData(loginUtils.getAccessToken()!!, true)

        binding.onGoing.setOnClickListener{
            this.changeButton(binding.onGoing, binding.riwayat)
            binding.progressBarActivity.show()
            model.loadData(loginUtils.getAccessToken()!!, true)
        }

        binding.riwayat.setOnClickListener{
            this.changeButton(binding.riwayat, binding.onGoing)
            binding.progressBarActivity.show()
            model.loadData(loginUtils.getAccessToken()!!, false)
        }

        return binding.root
    }

    override fun onSuccess(items: Array<FireLocationResponse>) {
        val adapter = AdapterFireLocationList(items, geocoder)
        val layoutManager = LinearLayoutManager(context)
        binding.recyclerViewItems.layoutManager = layoutManager
        binding.recyclerViewItems.adapter = adapter
        binding.progressBarActivity.hide()
    }

    override fun onFailed() {
        Toast.makeText(context, "Something error please login again", Toast.LENGTH_LONG).show()
    }

    private fun changeButton(from: Button, to: Button) {
        to.setBackgroundColor(Color.parseColor("#ffffff"))
        to.setTextColor(Color.parseColor("#FF000000"))
        from.setBackgroundColor(Color.parseColor("#256AD0"))
        from.setTextColor(Color.parseColor("#ffffff"))
    }
}