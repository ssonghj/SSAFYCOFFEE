package com.ssafy.smartcafe.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartcafe.MobileCafeApplication
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.adapter.ProductsListAdapter
import com.ssafy.smartcafe.adapter.ShoppingListAdapter
import com.ssafy.smartcafe.adapter.SimpleCommentAdapter
import com.ssafy.smartcafe.adapter.UserLikeAdapter
import com.ssafy.smartcafe.databinding.ActivityUserLikeBinding
import com.ssafy.smartcafe.dto.ProductDTO
import com.ssafy.smartcafe.service.ProductService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

class UserLikeActivity : AppCompatActivity() {
    private lateinit var binding:ActivityUserLikeBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var userLikeAdapter : UserLikeAdapter
    private lateinit var productList:List<ProductDTO>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserLikeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener{
            finish()
        }

        setAdapter()
    }

    private fun setAdapter(){

        CoroutineScope(Dispatchers.Main).launch {
            getAllUserLikeMenu()

            // 1. ListView 객체 생성
            recyclerView = binding.recyclerUserDetailReview
            recyclerView.layoutManager = GridLayoutManager(applicationContext,3)
            OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL)

            // 2. Adapter 객체 생성(한 행을 위해 반복 생성할 Layout과 데이터 전달)
            userLikeAdapter = UserLikeAdapter(applicationContext, R.layout.item_user_like, productList)

            // 3. ListView와 Adapter 연결
            recyclerView.adapter = userLikeAdapter
        }
    }

    private suspend fun getAllUserLikeMenu() {
        withContext(Dispatchers.IO) {
            val service = MobileCafeApplication.retrofit.create(ProductService::class.java)
            val response = service.selectUserLikeMenu(LoginActivity.userId).execute()

            if (response.code() == 200) {
                productList = (response.body() as ArrayList<ProductDTO>?)!!
                println("getAllUserLikeMenu : ${productList}")
            } else {
                println("getAllUserLikeMenu: error code")
            }
        }
    }
}