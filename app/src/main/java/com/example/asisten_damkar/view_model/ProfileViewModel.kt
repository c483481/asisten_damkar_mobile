package com.example.asisten_damkar.view_model

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.asisten_damkar.listener.ProfileFragmentListener
import com.example.asisten_damkar.utils.LoginUtils

class ProfileViewModel: ViewModel() {
    lateinit var profileFragmentListener: ProfileFragmentListener
    fun onClickLogout(view: View) {
        profileFragmentListener.onClickLogout()
    }
}