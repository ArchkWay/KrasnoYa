package com.example.archek.krasnoya

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.widget.Button
import kotlinx.android.synthetic.main.first_activity.*

class FirstActivity : AppCompatActivity() {//входной экран
internal lateinit var btnForward: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.first_activity)
        btnForward = findViewById(R.id.btnForward)//кнопка вход - активирует основной экран
        btnForward.setOnClickListener { v -> startActivity() }

    }

    private fun startActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
