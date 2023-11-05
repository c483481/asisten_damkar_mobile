package com.example.asisten_damkar.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.asisten_damkar.R

class SuccessPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success_page)
        val button = findViewById<Button>(R.id.back_button_success)

        button.setOnClickListener{
            val i = Intent(this, HomeActivity::class.java)
            startActivity(i)
            finish()
        }
    }
}