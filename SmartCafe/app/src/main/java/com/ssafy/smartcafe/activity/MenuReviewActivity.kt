package com.ssafy.smartcafe.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartcafe.MobileCafeApplication
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.activity.LoginActivity.Companion.vibratePhone
import com.ssafy.smartcafe.adapter.MenuReviewAdapter
import com.ssafy.smartcafe.adapter.ProductsListAdapter
import com.ssafy.smartcafe.databinding.ActivityMenuReviewBinding
import com.ssafy.smartcafe.dto.ProductDTO
import com.ssafy.smartcafe.dto.UserDTO
import com.ssafy.smartcafe.service.ProductService
import com.ssafy.smartcafe.service.UserService
import kotlinx.coroutines.*
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

class MenuReviewActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var menuReviewAdapter : MenuReviewAdapter
    private lateinit var binding: ActivityMenuReviewBinding
    private var productList = arrayListOf<ProductDTO>()
    private var product_id = 0

    override fun onResume() {
        super.onResume()
        setAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        product_id = intent!!.getStringExtra("product_id")!!.toInt()
        var product_name = intent!!.getStringExtra("product_name")

        binding.tvMenuName.text = product_name

        //뒤로가기
        binding.btnBack.setOnClickListener{
            vibratePhone(application)
            finish()
        }

        setAdapter()

    }

    private fun setAdapter(){
        CoroutineScope(Dispatchers.Main).launch {
            getComment()

            // 1. ListView 객체 생성
            recyclerView = binding.recyclerDetailReview
            recyclerView.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL)

            // 2. Adapter 객체 생성(한 행을 위해 반복 생성할 Layout과 데이터 전달)
            menuReviewAdapter = MenuReviewAdapter(applicationContext, R.layout.item_detail_review, productList,product_id)

            // 3. ListView와 Adapter 연결
            recyclerView.adapter = menuReviewAdapter
        }

    }

     private suspend fun getComment() {
        withContext(Dispatchers.IO) {
            val service = MobileCafeApplication.retrofit.create(ProductService::class.java)
            val response = service.selectProduct(product_id).execute()

            if (response.code() == 200) {
                productList = (response.body() as ArrayList<ProductDTO>?)!!

                println("MenuReviewActivity : ${productList}")
            } else {
                print("MenuReviewActivity: error code")
            }
        }
    }
}