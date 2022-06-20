package com.ssafy.smartcafe.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.adapter.ShoppingListAdapter
import com.ssafy.smartcafe.adapter.SimpleCommentAdapter
import com.ssafy.smartcafe.databinding.ActivityUserLikeBinding

class UserLikeActivity : AppCompatActivity() {
    private lateinit var binding:ActivityUserLikeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserLikeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener{
            finish()
        }
    }
}