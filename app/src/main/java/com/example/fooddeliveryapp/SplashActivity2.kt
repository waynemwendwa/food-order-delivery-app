package com.example.fooddeliveryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.ActionBar

class SplashActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash2)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()

        Handler().postDelayed( {
            startActivity(Intent(this@SplashActivity2, MainActivity::class.java ))
            finish()
        }, 2000)
    }
}