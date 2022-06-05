package com.ssafy.smartcafe.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.databinding.ActivityMenuDetailBinding

class MenuDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvMoreDetailReview.setOnClickListener{
            val intent = Intent(this, MenuReviewActivity::class.java)
            startActivity(intent)
        }
    }
}