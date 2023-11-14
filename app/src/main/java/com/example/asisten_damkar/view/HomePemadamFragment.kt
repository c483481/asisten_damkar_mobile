package com.example.asisten_damkar.view

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asisten_damkar.R
import com.example.asisten_damkar.adapter.AdapterFireLocationDetail
import com.example.asisten_damkar.adapter.AdapterPemadamFireLocationDetail
import com.example.asisten_damkar.databinding.FragmentHomePemadamBinding
import com.example.asisten_damkar.listener.HomePemadamFragmentListener
import com.example.asisten_damkar.listener.HomePemadamSocketListener
import com.example.asisten_damkar.listener.OnClickAdapter
import com.example.asisten_damkar.response.FireLocationResponse
import com.example.asisten_damkar.response.ResponseList
import com.example.asisten_damkar.utils.LoginUtils
import com.example.asisten_damkar.utils.SocketPemadamUtils
import com.example.asisten_damkar.utils.hide
import com.example.asisten_damkar.view_model.HomePemadamViewModel
import java.util.Locale

class HomePemadamFragment : Fragment(), HomePemadamFragmentListener, HomePemadamSocketListener {
    private lateinit var binding: FragmentHomePemadamBinding
    private lateinit var geocoder: Geocoder
    private var countFireLocation = 0
    private var items: Array<FireLocationResponse> = arrayOf()
    private lateinit var adapter: AdapterPemadamFireLocationDetail
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomePemadamBinding.inflate(inflater)

        geocoder = Geocoder(container!!.context, Locale.getDefault())

        val viewModel = ViewModelProvider(this)[HomePemadamViewModel::class.java]

        val loginUtils = LoginUtils(container.context)

        viewModel.socketPemadamUtils = SocketPemadamUtils()

        viewModel.homePemadamSocketListener = this

        viewModel.homePemadamFragmentListener = this

        val listener: OnClickAdapter<FireLocationResponse> =
            object : OnClickAdapter<FireLocationResponse> {
                override fun onClickAdapter(data: FireLocationResponse) {
                    val intent = Intent(context, MapsActivity::class.java)
                    intent.putExtra("lng", data.lng)
                    intent.putExtra("lat", data.lat)
                    intent.putExtra("fireLocationXid", data.xid)
                    intent.putExtra("topic", "fireLocation")
                    intent.putExtra("createdAt", data.createdAt)
                    startActivity(intent)
                }
            }

        adapter = AdapterPemadamFireLocationDetail(items, geocoder, listener)

        viewModel.createSocketConnection(loginUtils.getPosXid()!!)

        viewModel.fetchFireLocation(loginUtils.getAccessToken()!!,loginUtils.getPosXid()!!)

        return binding.root
    }

    override fun onCallbackFireList(data: LiveData<ResponseList<FireLocationResponse>?>) {
        data.observe(this, Observer {
            if(it == null) {
                Toast.makeText(context, "Ada kesalahan, Silahkan login kembali.", Toast.LENGTH_LONG).show();
                return@Observer
            }
            countFireLocation = it.count
            items = it.items

            binding.infoText.text = "Jumlah Kebakaran saat ini : ${countFireLocation}"
            if(it.count == 0) {
                binding.infoKosong.visibility = View.VISIBLE
                return@Observer
            }
            adapter.updateData(items)
            val layoutManager = LinearLayoutManager(context)
            binding.recyclerViewItems.layoutManager = layoutManager
            binding.recyclerViewItems.adapter = adapter
        })
    }

    override fun onConnected() {
        activity?.runOnUiThread {
            Toast.makeText(context, "Success Connect to server", Toast.LENGTH_LONG).show()
        }
    }

    override fun onPushFireLocation(data: FireLocationResponse) {
        activity?.runOnUiThread{
            countFireLocation += 1
            binding.infoText.text = "Jumlah Kebakaran saat ini : ${countFireLocation}"
            items = arrayOf(data, *items.copyOf())
            adapter.updateData(items)
        }
    }
}