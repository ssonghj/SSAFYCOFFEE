package com.ssafy.smartcafe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ssafy.smartcafe.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val joinButton = findViewById<TextView>(R.id.tv_join)

        joinButton.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY))
        }
    }
}