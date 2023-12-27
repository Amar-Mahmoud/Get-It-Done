package com.example.todolistapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_get_started_screen.*
import kotlinx.android.synthetic.main.activity_start_screen2.*

class GetStartedScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_started_screen)
        supportActionBar?.hide()

        btnGetStarted2.setOnClickListener {
            val i = Intent(this, StartScreen::class.java)
            startActivity(i)

        }


    }
}