package com.example.asisten_damkar.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.asisten_damkar.R
import com.example.asisten_damkar.databinding.ActivityAddItemsBinding
import com.example.asisten_damkar.listener.AddItemsListener
import com.example.asisten_damkar.utils.LoginUtils
import com.example.asisten_damkar.utils.hide
import com.example.asisten_damkar.utils.show
import com.example.asisten_damkar.utils.toast
import com.example.asisten_damkar.view_model.ItemsFragmentViewModel

class AddItemsActivity : AppCompatActivity(), AddItemsListener {
    lateinit var binding: ActivityAddItemsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_items)
        val model = ViewModelProvider(this)[ItemsFragmentViewModel::class.java]
        model.addItemsListener = this
        model.lifecycle = this
        model.loginUtils = LoginUtils(this)

        binding.submitted.setOnClickListener {
            binding.progressBarItems.show()
            model.addItems(binding.itemsName.text.toString())
        }
    }

    override fun onNotValid() {
        binding.progressBarItems.hide()
        toast("please enter the name")
    }

    override fun onSubmitted(data: Boolean) {
        binding.progressBarItems.hide()
        if(data) {
            toast("berhasil menambahkan items")
            val i = Intent(this, HomePemadamActivity::class.java)
            startActivity(i)
            return
        }
        toast("Gagal menambahkan items")
    }
}