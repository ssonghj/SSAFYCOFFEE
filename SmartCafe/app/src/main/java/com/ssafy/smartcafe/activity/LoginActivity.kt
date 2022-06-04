package com.ssafy.smartcafe.activity

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvJoin.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY))
        }

        binding.frameLoginBtn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java);
            startActivity(intent)
        }
    }
}