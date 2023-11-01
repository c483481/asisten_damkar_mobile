package com.example.asisten_damkar.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.asisten_damkar.databinding.FragmentProfileBinding
import com.example.asisten_damkar.listener.ProfileFragmentListener
import com.example.asisten_damkar.utils.LoginUtils
import com.example.asisten_damkar.view_model.ProfileViewModel

class ProfileFragment : Fragment(), ProfileFragmentListener {
    lateinit var container: ViewGroup
    lateinit var loginUtils: LoginUtils
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileBinding.inflate(inflater)

        val viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        loginUtils = LoginUtils(container!!.context)

        viewModel.profileFragmentListener = this

        binding.model = viewModel

        this.container = container!!

        when(loginUtils.getRole()) {
            "CEN" -> {
                binding.role.text = "Operator Central"
            }
            "ADM" -> {
                binding.role.text = "Admin"
            }
            "PEM" -> {
                binding.role.text = "Pemadam"
            }
            else -> {
                binding.role.text = "Error"
            }
        }

        return binding.root
    }

    override fun onClickLogout() {
        loginUtils.logOut()
        startActivity(Intent(this.container.context, LoginActivity::class.java))
    }
}