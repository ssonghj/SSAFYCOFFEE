package com.ssafy.smartcafe.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartcafe.MobileCafeApplication
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.activity.LoginActivity.Companion.detailList
import com.ssafy.smartcafe.activity.LoginActivity.Companion.userId
import com.ssafy.smartcafe.adapter.ShoppingListAdapter
import com.ssafy.smartcafe.databinding.ActivityShoppingListBinding
import com.ssafy.smartcafe.dto.*
import com.ssafy.smartcafe.service.OrderService
import com.ssafy.smartcafe.service.ProductService
import com.ssafy.smartcafe.util.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.co.bootpay.Bootpay
import kr.co.bootpay.BootpayAnalytics
import kr.co.bootpay.enums.Method
import kr.co.bootpay.enums.PG
import kr.co.bootpay.enums.UX
import kr.co.bootpay.model.BootExtra
import kr.co.bootpay.model.BootUser
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import java.util.*
import kotlin.collections.ArrayList

class ShoppingListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShoppingListBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var shoppingListAdapter : ShoppingListAdapter
    private var allProductList = arrayListOf<ProductDTO>()
    private var needProductList = arrayListOf<ShoppingListDTO>()
    private lateinit var order:OrderDTO

    val application_id = "62b072d0e38c3000235ae5c6"

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
        BootpayAnalytics.init(this, application_id)
        binding.frameBuy.setOnClickListener{
            //부트페이 연결
            goBootpayRequest() //잠깐 주석 처리
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

    fun goBootpayRequest() {
        val bootUser = BootUser().setPhone("010-1234-5678")
        val bootExtra = BootExtra().setQuotas(intArrayOf(0, 2, 3))

        val stuck = 1 //재고 있음

        Bootpay.init(this)
            .setApplicationId(application_id) // 해당 프로젝트(안드로이드)의 application id 값
            .setContext(this)
            .setBootUser(bootUser)
            .setBootExtra(bootExtra)
            .setUX(UX.PG_DIALOG)
            .setPG(PG.INICIS)
            .setMethod(Method.CARD)
//                .setUserPhone("010-1234-5678") // 구매자 전화번호
            .setName("SmartStore 주문") // 결제할 상품명
            .setOrderId("2022") // 결제 고유번호expire_month
            .setPrice(100) // 결제할 금액
            .addItem("${needProductList[0].name} 등", 1, "${needProductList[0].name}", 100) // 주문정보에 담길 상품정보, 통계를 위해 사용
//            .addItem("키보드", 1, "ITEM_CODE_KEYBOARD", 2, "패션", "여성상의", "블라우스") // 주문정보에 담길 상품정보, 통계를 위해 사용
            .onConfirm { message ->
                if (0 < stuck) Bootpay.confirm(message); // 재고가 있을 경우.
                else Bootpay.removePaymentWindow(); // 재고가 없어 중간에 결제창을 닫고 싶을 경우
                Log.d("confirm", message);
            }
            .onDone { message ->
                Log.d("done", message)

                //주문시간 저장
                val sdf = Utils.formatter()
                val date = sdf.format(System.currentTimeMillis())

                //스탬프
                var sum_quantity = 0
                for(i in detailList.indices){
                    sum_quantity += detailList[i].quantity
                }
                var stamp = StampDTO(0, 0,sum_quantity,userId)

                //서버로 오더내용 보내기
                order = OrderDTO('N', detailList, 0, "order_table_01",
                    date, stamp ,userId)

                goNext()

                //창닫기
                finish()
            }
            .onReady { message ->
                Log.d("ready", message)
            }
            .onCancel { message ->
                Log.d("cancel", message)
            }
            .onError{ message ->
                Log.d("error", message)
            }
            .onClose { message ->
                Log.d("close", "close")
            }
            .request();
    }

    private fun goNext(){
        CoroutineScope(Dispatchers.Main).launch {
            sendOrderInfo()
            //쇼핑내용지우기
            detailList.clear()
        }
    }

    private suspend fun sendOrderInfo() {
        withContext(Dispatchers.IO) {
            val service = MobileCafeApplication.retrofit.create(OrderService::class.java)
            val response = service.insertOrder(order)

            if (response.code() == 200) {
                var res = response.body()!!

                println("sendOrderInfo : ${res}")
            } else {
                println("sendOrderInfo: error code")
            }
        }
    }
}