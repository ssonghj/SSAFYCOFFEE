package com.ssafy.smartcafe.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartcafe.MobileCafeApplication
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.activity.LoginActivity.Companion.userId
import com.ssafy.smartcafe.adapter.CurOrderAdapter
import com.ssafy.smartcafe.adapter.ShoppingListAdapter
import com.ssafy.smartcafe.databinding.ActivityDetailCurOrderMenuBinding
import com.ssafy.smartcafe.dto.ProductDTO
import com.ssafy.smartcafe.dto.RecentOrderDTO
import com.ssafy.smartcafe.service.OrderService
import com.ssafy.smartcafe.service.UserService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

class DetailCurOrderMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailCurOrderMenuBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var curOrderListAdapter : CurOrderAdapter
    private var productList = arrayListOf<RecentOrderDTO>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCurOrderMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //뒤로가기
        binding.btnBack.setOnClickListener{
            finish()
        }

        CoroutineScope(Dispatchers.Main).launch {
            getCurOrder()
            setAdapter()

            var sum = 0
            for(i in productList.indices){
                sum += productList[i].price * productList[i].quantity
            }
            binding.tvTotalPrice.text = "총액 : ${sum}원"
        }
    }

    private fun setAdapter(){
        // 1. ListView 객체 생성
        recyclerView = binding.recyclerCurOrder
        recyclerView.layoutManager = LinearLayoutManager(
            applicationContext,
            LinearLayoutManager.VERTICAL, false
        )

        //클릭시 효과 안나오게 하는 것
//        recyclerView.itemAnimator = null
        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL)

        // 2. Adapter 객체 생성(한 행을 위해 반복 생성할 Layout과 데이터 전달)
        curOrderListAdapter = CurOrderAdapter(applicationContext, R.layout.item_cur_order, productList)

        // 3. ListView와 Adapter 연결
        recyclerView.adapter = curOrderListAdapter
    }

    private suspend fun getCurOrder() {
        withContext(Dispatchers.IO) {
            val service = MobileCafeApplication.retrofit.create(OrderService::class.java)
            val response = service.selectCurOrder(userId).execute()

            if (response.code() == 200) {
                productList = (response.body() as ArrayList<RecentOrderDTO>?)!!
                println("getCurOrder : ${productList}")
            } else {
                println("getCurOrder: error code")
            }
        }
    }

}