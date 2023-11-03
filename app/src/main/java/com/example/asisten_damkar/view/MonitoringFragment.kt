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
import com.example.asisten_damkar.adapter.PosListAdapter
import com.example.asisten_damkar.databinding.FragmentMonitoringBinding
import com.example.asisten_damkar.listener.MonitoringListener
import com.example.asisten_damkar.listener.OnClickAdapter
import com.example.asisten_damkar.response.PosResponse
import com.example.asisten_damkar.response.ResponseList
import com.example.asisten_damkar.response.Truck
import com.example.asisten_damkar.utils.LoginUtils
import com.example.asisten_damkar.utils.hide
import com.example.asisten_damkar.utils.show
import com.example.asisten_damkar.view_model.MonitoringViewModel

class MonitoringFragment : Fragment(), MonitoringListener {
    lateinit var loginUtils: LoginUtils
    lateinit var binding: FragmentMonitoringBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMonitoringBinding.inflate(inflater)
        loginUtils = LoginUtils(container!!.context)
        val model = ViewModelProvider(this)[MonitoringViewModel::class.java]
        model.monitoringListener = this
        model.lifecycleOwner = this
        model.loginUtils = LoginUtils(container.context)

        binding.progressBarPos.show()
        model.getAllPos()

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onSuccessFetch(data: ResponseList<PosResponse>) {
        binding.progressBarPos.hide()
        binding.scrollViewPos.visibility = View.VISIBLE

        val listener = object : OnClickAdapter<PosResponse> {
            override fun onClickAdapter(data: PosResponse) {
                val i = Intent(context, PosDetailActivity::class.java)
                i.putExtra("posXid", data.xid)
                startActivity(i)
            }
        }

        val adapter = PosListAdapter(data.items, listener)
        val layoutManager = LinearLayoutManager(context)
        binding.posDetailRecyclerView.layoutManager = layoutManager
        binding.posDetailRecyclerView.adapter = adapter
    }

    override fun onFailed() {
        binding.progressBarPos.hide()
        Toast.makeText(context, "Failed to load data, please login again", Toast.LENGTH_LONG).show()
    }
}