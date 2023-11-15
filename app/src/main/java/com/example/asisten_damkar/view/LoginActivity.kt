package com.example.asisten_damkar.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.asisten_damkar.R
import com.example.asisten_damkar.databinding.ActivityLoginBinding
import com.example.asisten_damkar.listener.LoginListener
import com.example.asisten_damkar.response.LoginResponse
import com.example.asisten_damkar.response.PemadamResponse
import com.example.asisten_damkar.utils.LoginUtils
import com.example.asisten_damkar.utils.hide
import com.example.asisten_damkar.utils.show
import com.example.asisten_damkar.utils.toast
import com.example.asisten_damkar.view_model.LoginViewModel

class LoginActivity: AppCompatActivity(), LoginListener {
    var binding: ActivityLoginBinding? = null
    var loginUtils: LoginUtils? = null
    private lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginUtils = LoginUtils(this)
        loginUtils!!.checkIsLogin()

        binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding?.model = viewModel
        viewModel.loginListener = this
    }

    override fun onStarted() {
        binding?.progressBar?.show()
    }

    override fun fallback(success: LiveData<LoginResponse?>) {
        success.observe(this, Observer {

            binding!!.progressBar.hide()
            if(it == null) {
              this.show();
              return@Observer
            }
            loginUtils!!.createLoginSession(it)
            if(it.tagRole == "PEM") {
                viewModel.getPemadamInfo(it.key.accessToken.token, it.xid)
                return@Observer
            }
            startActivity(Intent(this, HomeActivity::class.java))
        })
    }

    override fun fallbackPemadam(success: LiveData<PemadamResponse?>) {
        success.observe(this, Observer {
            binding!!.progressBar.hide()
            if(it?.pos == null || it?.truck == null) {
                toast("data tidak valid, mohon hubungi admin")
                return@Observer
            }
            loginUtils!!.createPemadamSession(it.pos.xid, it.truck.xid)
            startActivity(Intent(this, HomePemadamActivity::class.java))
        })
    }

    override fun onNotValid() {
        binding?.progressBar?.hide()
        this.show()
    }

    private fun show() {
        binding?.errPass?.show()
        binding?.errUsername?.show()
    }

}