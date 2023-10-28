package com.example.asisten_damkar.view_model

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.asisten_damkar.listener.LoginListener
import com.example.asisten_damkar.network.LoginNetwork
import com.example.asisten_damkar.repository.LoginRepository

class LoginViewModel: ViewModel() {
    var username: String? = null
    var password: String? = null

    var loginListener: LoginListener? = null
    fun onClickLoginButton(view: View) {
        loginListener?.onStarted()
        if(username.isNullOrEmpty() || password.isNullOrEmpty()) {
            loginListener?.onNotValid("Invalid Email Or Password")
            return
        }
        val loginResponse = LoginRepository().login(username!!, password!!)
        loginListener?.onDone(loginResponse)
    }
}