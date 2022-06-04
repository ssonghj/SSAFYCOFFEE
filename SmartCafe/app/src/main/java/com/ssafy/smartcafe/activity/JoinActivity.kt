package com.ssafy.smartcafe.activity

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.ssafy.smartcafe.R

class JoinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        var back = findViewById<ImageView>(R.id.iv_back)
        back.setOnClickListener {
            finish()
        }

    }
}