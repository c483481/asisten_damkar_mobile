package com.example.asisten_damkar.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.example.asisten_damkar.R
import com.example.asisten_damkar.databinding.ActivityLoginBinding
import com.example.asisten_damkar.listener.LoginListener
import com.example.asisten_damkar.utils.toast
import com.example.asisten_damkar.view_model.LoginViewModel

class LoginActivity: AppCompatActivity(), LoginListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        val viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.model = viewModel

        viewModel.loginListener = this
    }

    override fun onStarted() {
        toast("started")
    }

    override fun onDone(message: LiveData<String>) {
        message.observe(this) { it ->
            toast(it)
        }
    }

    override fun onNotValid(message: String) {
        toast(message)
    }
}