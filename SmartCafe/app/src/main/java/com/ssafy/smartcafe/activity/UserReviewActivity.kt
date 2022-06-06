package com.ssafy.smartcafe.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.databinding.ActivityUserReviewBinding
import com.ssafy.smartcafe.databinding.FragmentOrderBinding

class UserReviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserReviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}