package com.example.asisten_damkar.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asisten_damkar.R
import com.example.asisten_damkar.adapter.ItemsListAdapter
import com.example.asisten_damkar.databinding.ActivityTruckStatusBinding
import com.example.asisten_damkar.listener.OnClickAdapter
import com.example.asisten_damkar.listener.TruckStatusListener
import com.example.asisten_damkar.response.Item
import com.example.asisten_damkar.response.TruckDetailResponse
import com.example.asisten_damkar.response.removeByXid
import com.example.asisten_damkar.response.updateByXid
import com.example.asisten_damkar.utils.LoginUtils
import com.example.asisten_damkar.utils.getStringDate
import com.example.asisten_damkar.utils.hide
import com.example.asisten_damkar.utils.show
import com.example.asisten_damkar.utils.toast
import com.example.asisten_damkar.view_model.TruckStatusViewModel

class TruckStatusActivity : AppCompatActivity(), TruckStatusListener {

    lateinit var binding: ActivityTruckStatusBinding
    lateinit var adapter: ItemsListAdapter
    lateinit var items: List<Item>
    lateinit var model: TruckStatusViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_truck_status)

        binding.backButton.setOnClickListener{
            val i = Intent(this, HomeActivity::class.java)
            startActivity(i)
            finish()
        }

        val xid = intent.getStringExtra("truckXid")!!
        model = ViewModelProvider(this)[TruckStatusViewModel::class.java]
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

        val listenerOnUpdate = object: OnClickAdapter<Item> {
            override fun onClickAdapter(data: Item) {
                model.updateItem(data.xid)
            }
        }

        val listenerOnDelete = object: OnClickAdapter<Item> {
            override fun onClickAdapter(data: Item) {
                model.deleteItem(data.xid)
            }
        }


        items = data.items
        adapter = ItemsListAdapter(items, listenerOnDelete = listenerOnDelete, listenerOnUpdate = listenerOnUpdate)
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerViewItems.layoutManager = layoutManager
        binding.recyclerViewItems.adapter = adapter
    }

    override fun onFailed() {
        toast("ada yang salah, silahkan login kembali...")
    }

    override fun loading() {
        binding.progressBarTruck.show()
    }

    override fun clear() {
        binding.progressBarTruck.hide()
    }

    override fun update(data: Boolean, xid: String) {
        if(data) {
            toast("data berhasil di update")
            items = items.updateByXid(xid)
            adapter.updateItems(items)
            return
        }
        toast("ada yang salah, silahkan login kembali...")
    }

    override fun delete(data: Boolean, xid: String) {
        if(data) {
            toast("data berhasil di hapus")
            items = items.removeByXid(xid)
            adapter.updateItems(items)
            return
        }
        toast("ada yang salah, silahkan login kembali...")
    }
}