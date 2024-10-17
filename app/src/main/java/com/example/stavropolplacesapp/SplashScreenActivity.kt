package com.example.stavropolplacesapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen) // Убедитесь, что у вас есть этот макет

        // Переход к главной активности через 2 секунды
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Закрываем SplashScreenActivity
        }, 2000) // 2000 миллисекунд (2 секунды)
    }
}