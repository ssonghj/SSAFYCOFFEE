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
import com.ssafy.smartcafe.adapter.PastOrderAdapter
import com.ssafy.smartcafe.databinding.ActivityUserRecentOrderListBinding
import com.ssafy.smartcafe.dto.ProductDTO
import com.ssafy.smartcafe.dto.RecentOrderDTO
import com.ssafy.smartcafe.service.OrderService
import com.ssafy.smartcafe.service.ProductService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import java.util.LinkedHashMap

class UserRecentOrderListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserRecentOrderListBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var pastOrderListAdapter : PastOrderAdapter
    private var pastOrderList = arrayListOf<RecentOrderDTO>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserRecentOrderListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener{
            finish()
        }

        CoroutineScope(Dispatchers.Main).launch {

            getPastOrder()

            var map = LinkedHashMap<Int, MutableList<RecentOrderDTO>>()
            var list = mutableListOf<RecentOrderDTO>()
            var mapKey = mutableListOf<Int>()

            for(i in pastOrderList.indices){
                if(list.isEmpty()){
                    list.add(pastOrderList[i])
                }else{
                    if(list[0].o_id == pastOrderList[i].o_id){
                        list.add(pastOrderList[i])
                    }else if(list[0].o_id != pastOrderList[i].o_id){
                        mapKey.add(list[0].o_id)
                        map.put(list[0].o_id,list)
                        list = mutableListOf()
                        list.add(pastOrderList[i])
                    }
                }
            }

            setAdapter(map,mapKey)

            //다 넣고 나면
            if(list.size!=0) {
                mapKey.add(list[0].o_id)
                map.put(list[0].o_id, list)
                list = mutableListOf()
            }

        }

    }

    private fun setAdapter(map:LinkedHashMap<Int, MutableList<RecentOrderDTO>> , mapKey: MutableList<Int>){
        // 1. ListView 객체 생성
        recyclerView = binding.recyclerPastOrderList
        recyclerView.layoutManager = LinearLayoutManager(
            applicationContext,
            LinearLayoutManager.VERTICAL, false
        )

        //클릭시 효과 안나오게 하는 것
        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL)

        // 2. Adapter 객체 생성(한 행을 위해 반복 생성할 Layout과 데이터 전달)
        pastOrderListAdapter = PastOrderAdapter(applicationContext, R.layout.item_recent_shopping_list, map,mapKey)

        // 3. ListView와 Adapter 연결
        recyclerView.adapter = pastOrderListAdapter
    }

    private suspend fun getPastOrder() {
        withContext(Dispatchers.IO) {
            val service = MobileCafeApplication.retrofit.create(OrderService::class.java)
            val response = service.selectPastOrder(userId).execute()

            if (response.code() == 200) {
                pastOrderList = (response.body() as ArrayList<RecentOrderDTO>?)!!

                println("getPastOrder : ${pastOrderList}")
            } else {
                print("getPastOrder: error code")
            }
        }
    }
}