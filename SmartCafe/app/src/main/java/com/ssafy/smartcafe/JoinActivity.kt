package com.ssafy.smartcafe

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity

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