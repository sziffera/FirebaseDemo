package com.example.szian17_hw03_firebase

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()

    }
}
