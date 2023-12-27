package com.example.todolistapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_start_screen2.*

class StartScreen : AppCompatActivity() {
    var userName :String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        val animationZoomIn = AnimationUtils.loadAnimation(this, R.anim.zoomin)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_screen2)
        supportActionBar?.hide()
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val savedString = sharedPreferences.getString("STRING_KEY", null)
        /*if (savedString != null) {
            if(savedString.isNotEmpty()){
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
            }
        }*/

        //loadData()

    btnNext.setOnClickListener {
        saveData()
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)

    }






    }

    private fun saveData(){
        val userName : String = textInputEditText.text.toString()
        val boolValue: Boolean = cbNotifications.isChecked
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("STRING_KEY", userName)
            putBoolean("BOOL_KEY", boolValue)
        }.apply()

    }

    private fun loadData(){
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val savedString = sharedPreferences.getString("STRING_KEY", null)
        val savedBoolean = sharedPreferences.getBoolean("BOOL_KEY", false)

    }


}
