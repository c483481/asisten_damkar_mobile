package com.example.asisten_damkar.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.asisten_damkar.R
import com.example.asisten_damkar.utils.LoginUtils
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomePemadamActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_pemadam)
        val loginUtils = LoginUtils(this)

        loginUtils.checkIsNotLogin()

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = Navigation.findNavController(this, R.id.host_fragment)

        NavigationUI.setupWithNavController(bottomNavigation, navController)
    }
}