package com.example.asisten_damkar.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.asisten_damkar.R
import com.example.asisten_damkar.utils.LoginUtils

class MonitoringFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val loginActivity = LoginUtils(container!!.context)

        Toast.makeText(context, loginActivity.getAccessToken().toString(), Toast.LENGTH_SHORT).show()

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_monitoring, container, false)
    }
}