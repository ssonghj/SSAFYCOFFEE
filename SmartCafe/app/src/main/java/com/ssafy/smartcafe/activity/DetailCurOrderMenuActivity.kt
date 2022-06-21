package com.ssafy.smartcafe.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.databinding.ActivityDetailCurOrderMenuBinding

class DetailCurOrderMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailCurOrderMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCurOrderMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //뒤로가기
        binding.btnBack.setOnClickListener{
            finish()
        }


    }
}