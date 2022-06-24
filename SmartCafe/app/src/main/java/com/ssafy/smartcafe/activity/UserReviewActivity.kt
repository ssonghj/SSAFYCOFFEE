package com.ssafy.smartcafe.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartcafe.MobileCafeApplication
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.activity.LoginActivity.Companion.userId
import com.ssafy.smartcafe.adapter.MyReviewAdapter
import com.ssafy.smartcafe.adapter.SimpleCommentAdapter
import com.ssafy.smartcafe.databinding.ActivityUserReviewBinding
import com.ssafy.smartcafe.databinding.FragmentOrderBinding
import com.ssafy.smartcafe.dto.ProductDTO
import com.ssafy.smartcafe.service.ProductService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

class UserReviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserReviewBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var myReviewAdapter : MyReviewAdapter
    private var productList = arrayListOf<ProductDTO>()

    override fun onResume() {
        super.onResume()

        CoroutineScope(Dispatchers.Main).launch {
            getAllMyComment()
            setAdapter()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //뒤로가기
        binding.btnBack.setOnClickListener{
            finish()
        }

        //리뷰 모두 불러오기
        CoroutineScope(Dispatchers.Main).launch {
            getAllMyComment()
            setAdapter()
        }

    }

    private fun setAdapter(){
        // 1. ListView 객체 생성
        recyclerView = binding.recyclerUserDetailReview
        recyclerView.layoutManager = LinearLayoutManager(
            applicationContext,
            LinearLayoutManager.VERTICAL, false
        )
        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL)

        // 2. Adapter 객체 생성(한 행을 위해 반복 생성할 Layout과 데이터 전달)
        myReviewAdapter = MyReviewAdapter(applicationContext, R.layout.item_detail_review, productList)

        // 3. ListView와 Adapter 연결
        recyclerView.adapter = myReviewAdapter
    }

    private suspend fun getAllMyComment() {
        withContext(Dispatchers.IO) {
            val service = MobileCafeApplication.retrofit.create(ProductService::class.java)
            val response = service.selectAllProductOfUserComment(userId).execute()

            if (response.code() == 200) {
                productList = (response.body() as ArrayList<ProductDTO>?)!!

                println("getAllMyComment : ${productList}")
            } else {
                print("getAllMyComment: error code")
            }
        }
    }
}