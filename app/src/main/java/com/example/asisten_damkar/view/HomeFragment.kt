package com.example.asisten_damkar.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.asisten_damkar.databinding.FragmentHomeBinding
import com.example.asisten_damkar.listener.HomeFragmentListener
import com.example.asisten_damkar.view_model.HomeViewModel

class HomeFragment : Fragment(), HomeFragmentListener {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater)

        val viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding.model = viewModel

        viewModel.homeListener = this

        return binding.root
    }

    override fun handleTitikKebakaran() {
        val i = Intent(context, SearchKebakaranActivity::class.java)
        startActivity(i)
    }

    override fun handlePosTracker() {
        val i = Intent(context, MapsActivity::class.java)
        i.putExtra("topic", "tracker")
        startActivity(i)
    }
}