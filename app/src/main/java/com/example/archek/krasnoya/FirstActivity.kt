package com.example.archek.krasnoya

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView

class FirstActivity : AppCompatActivity() {//входной экран
    internal lateinit var ivForward: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.first_activity)
        ivForward = findViewById(R.id.ivForward)//кнопка вход - активирует основной экран
        ivForward.setOnClickListener { v -> startActivity() }

    }

    private fun startActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
