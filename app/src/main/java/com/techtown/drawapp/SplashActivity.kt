package com.techtown.drawapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.lang.Exception

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        try{
            Thread.sleep(3000)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }catch (e : Exception){
            e.printStackTrace()
        }
    }
}
