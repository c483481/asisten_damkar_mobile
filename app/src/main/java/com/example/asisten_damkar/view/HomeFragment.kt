package com.example.asisten_damkar.view

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asisten_damkar.adapter.AdapterFireLocationDetail
import com.example.asisten_damkar.adapter.PosListAdapter
import com.example.asisten_damkar.databinding.FragmentHomeBinding
import com.example.asisten_damkar.listener.HomeFragmentListener
import com.example.asisten_damkar.response.FireLocationResponse
import com.example.asisten_damkar.utils.LoginUtils
import com.example.asisten_damkar.utils.hide
import com.example.asisten_damkar.utils.show
import com.example.asisten_damkar.view_model.HomeViewModel
import java.util.Locale

class HomeFragment : Fragment(), HomeFragmentListener {
    lateinit var binding: FragmentHomeBinding
    lateinit var geocoder: Geocoder
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        geocoder = Geocoder(container!!.context, Locale.getDefault())

        val viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding.model = viewModel

        viewModel.homeListener = this
        viewModel.lifecycleOwner = this

        val loginUtils = LoginUtils(container!!.context)

        binding.progressBarHome.show()
        viewModel.fetchFirstData(loginUtils.getAccessToken()!!)

        return binding.root
    }

    override fun handleTitikKebakaran() {
        val i = Intent(context, SearchKebakaranActivity::class.java)
        startActivity(i)
    }

    override fun handlePosTracker() {
        val i = Intent(context, MapsActivity::class.java)
        i.putExtra("topic", "tracker")
        startActivity(i)
    }

    override fun onCallbackResponse(data: Array<FireLocationResponse>) {
        val adapter = AdapterFireLocationDetail(data, geocoder)
        val layoutManager = LinearLayoutManager(context)
        binding.homeRecyclerView.layoutManager = layoutManager
        binding.homeRecyclerView.adapter = adapter
        binding.progressBarHome.hide()
    }
}