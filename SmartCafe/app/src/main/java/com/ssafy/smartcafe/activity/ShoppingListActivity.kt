package com.ssafy.smartcafe.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartcafe.MobileCafeApplication
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.activity.LoginActivity.Companion.detailList
import com.ssafy.smartcafe.adapter.ShoppingListAdapter
import com.ssafy.smartcafe.databinding.ActivityShoppingListBinding
import com.ssafy.smartcafe.dto.ProductDTO
import com.ssafy.smartcafe.dto.ShoppingListDTO
import com.ssafy.smartcafe.service.ProductService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

class ShoppingListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShoppingListBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var shoppingListAdapter : ShoppingListAdapter
    private var allProductList = arrayListOf<ProductDTO>()
    private var needProductList = arrayListOf<ShoppingListDTO>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CoroutineScope(Dispatchers.Main).launch {
            getAllProduct()
            calculate()
        }

        //뒤로가기
        binding.btnBack.setOnClickListener {
            finish()
        }

        //결제하기
        binding.frameBuy.setOnClickListener{
            //부트페이 연결

            //결제 끝나면 서버로 주문 보내기
        }
    }

    private fun calculate(){
        println("allProductList.size : ${allProductList.size}")
        for(i in detailList.indices){
            for(j in allProductList.indices){

                if(detailList[i].productId == allProductList[j].id){

                    var list = ShoppingListDTO(
                        allProductList[j].name,
                        allProductList[j].price,
                        allProductList[j].price * detailList[i].quantity,
                        allProductList[j].img,
                        detailList[i].quantity
                    )

                    needProductList.add(list)
                    break
                }
            }
        }
        //화면 연결
        setAdapter()
    }

    private fun setAdapter(){

        // 1. ListView 객체 생성
        recyclerView = binding.recyclerUserDetailReview
        recyclerView.layoutManager = LinearLayoutManager(
            applicationContext,
            LinearLayoutManager.VERTICAL, false
        )
        recyclerView.itemAnimator = null
        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL)


        // 2. Adapter 객체 생성(한 행을 위해 반복 생성할 Layout과 데이터 전달)
        shoppingListAdapter = ShoppingListAdapter(
            applicationContext, R.layout.item_shopping_list, needProductList)

        // 3. ListView와 Adapter 연결
        recyclerView.adapter = shoppingListAdapter
    }

    private suspend fun getAllProduct() {
        withContext(Dispatchers.IO) {
            val service = MobileCafeApplication.retrofit.create(ProductService::class.java)
            val response = service.selectAllProduct().execute()

            if (response.code() == 200) {
                allProductList = (response.body() as ArrayList<ProductDTO>?)!!

                println("getAllProduct : ${allProductList}")
            } else {
                println("getAllProduct: error code")
            }
        }
    }
}