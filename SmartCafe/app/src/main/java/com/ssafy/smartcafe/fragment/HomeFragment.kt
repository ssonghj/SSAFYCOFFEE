package com.ssafy.smartcafe.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartcafe.MobileCafeApplication
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.activity.LoginActivity
import com.ssafy.smartcafe.adapter.*
import com.ssafy.smartcafe.databinding.FragmentHomeBinding
import com.ssafy.smartcafe.dto.OrderDTOwithTotal
import com.ssafy.smartcafe.dto.ProductDTO
import com.ssafy.smartcafe.dto.RecentOrderDTO
import com.ssafy.smartcafe.service.OrderService
import com.ssafy.smartcafe.service.ProductService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.LinkedHashMap

private const val TAG="HomeFragment"
class HomeFragment : Fragment() {

    private lateinit var ctx: Context
    private lateinit var binding: FragmentHomeBinding

    private lateinit var myListView: RecyclerView
    private lateinit var recentOrderListAdapter: RecentOrderListAdapter
    private lateinit var newProductListAdapter: NewProductListAdapter
    private lateinit var recommendedProductListAdapter: RecommendedProductListAdapter
    private lateinit var recommendedDesertListAdapter: RecommendedDesertListAdapter
    private lateinit var thisWeekTop3ListAdapter: ThisWeekTop3ListAdapter


    private var orderList:List<RecentOrderDTO> = arrayListOf()
    private var newProductList:List<ProductDTO> = arrayListOf()
    private var recommendedProductList:List<ProductDTO> = arrayListOf()
    private var recommendedDesertList:List<ProductDTO> = arrayListOf()
    private var thisWeekTop3List:List<OrderDTOwithTotal> = arrayListOf()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //context받아옴
        ctx = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        binding.tvUserName.text = LoginActivity.userName+"님"
        binding.tvEventForUser.text = LoginActivity.userName+"님을 위한 이벤트"
        binding.tvRecommendMenu.text = LoginActivity.userName+"님 이 메뉴들은 어때요?"

        //최근내역
        getRecentOrder(binding.root)

        //신상메뉴
        getNewProduct(binding.root)

        //추천메뉴
        getRecommendedProduct(binding.root)

        //이번주 top3
        getTop3Product(binding.root)

        //디저트 추천메뉴
        getRecommendedDesert(binding.root)

        return binding.root
    }

    private fun homeAdapter(inflater:View){
        CoroutineScope(Dispatchers.Main).launch {

            var map = LinkedHashMap<Int, MutableList<RecentOrderDTO>>()
            var list = mutableListOf<RecentOrderDTO>()
            var mapKey = mutableListOf<Int>()

            for(i in orderList.indices){
                Log.d(TAG, "homeAdapter: ${orderList[i]}")
                if(list.isEmpty()){
                    list.add(orderList[i])
                }else{
                    if(list[0].o_id == orderList[i].o_id){
                        list.add(orderList[i])
                    }else if(list[0].o_id != orderList[i].o_id){
                        mapKey.add(list[0].o_id)
                        map.put(list[0].o_id,list)
                        list = mutableListOf()
                        list.add(orderList[i])
                    }
                }
            }

            //다 넣고 나면
            if(list.size!=0) {
                mapKey.add(list[0].o_id)
                map.put(list[0].o_id, list)
                list = mutableListOf()
            }

            Log.d(TAG, "homeAdapter: ${mapKey}")
            println("list : ${list.size}")


            // 1. ListView 객체 생성
            myListView = inflater.findViewById(R.id.rv_recent_menu)

            myListView.layoutManager = LinearLayoutManager(ctx,
                LinearLayoutManager.HORIZONTAL, false)
            OverScrollDecoratorHelper.setUpOverScroll(myListView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL)



            // 2. Adapter 객체 생성(한 행을 위해 반복 생성할 Layout과 데이터 전달)
            recentOrderListAdapter = RecentOrderListAdapter(ctx, R.layout.recyclerview_recent_order_list,map,mapKey)

            // 3. ListView와 Adapter 연결
            myListView.adapter = recentOrderListAdapter
        }

    }

    private fun newProductAdapter(inflater: View){
        CoroutineScope(Dispatchers.Main).launch {

            // 1. ListView 객체 생성
            myListView = inflater.findViewById(R.id.rv_new_menu)
            myListView.layoutManager = LinearLayoutManager(ctx,
                LinearLayoutManager.HORIZONTAL, false)
            OverScrollDecoratorHelper.setUpOverScroll(myListView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL)


            // 2. Adapter 객체 생성(한 행을 위해 반복 생성할 Layout과 데이터 전달)
            newProductListAdapter = NewProductListAdapter(ctx, R.layout.item_ready, newProductList)

            // 3. ListView와 Adapter 연결
            myListView.adapter = newProductListAdapter

        }
    }

    private fun recommendedProductAdapter(inflater: View){
        CoroutineScope(Dispatchers.Main).launch {

            // 1. ListView 객체 생성
            myListView = inflater.findViewById(R.id.rv_recommend_menu)
            myListView.layoutManager = LinearLayoutManager(ctx,
                LinearLayoutManager.HORIZONTAL, false)
            OverScrollDecoratorHelper.setUpOverScroll(myListView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL)


            // 2. Adapter 객체 생성(한 행을 위해 반복 생성할 Layout과 데이터 전달)
            recommendedProductListAdapter = RecommendedProductListAdapter(ctx, R.layout.item_ready, recommendedProductList)

            // 3. ListView와 Adapter 연결
            myListView.adapter = recommendedProductListAdapter

        }
    }

    private fun recommendedDesertAdapter(inflater: View){
        CoroutineScope(Dispatchers.Main).launch {

            // 1. ListView 객체 생성
            myListView = inflater.findViewById(R.id.rv_recommend_desert)
            myListView.layoutManager = LinearLayoutManager(ctx,
                LinearLayoutManager.HORIZONTAL, false)
            OverScrollDecoratorHelper.setUpOverScroll(myListView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL)


            // 2. Adapter 객체 생성(한 행을 위해 반복 생성할 Layout과 데이터 전달)
            recommendedDesertListAdapter = RecommendedDesertListAdapter(ctx, R.layout.item_ready, recommendedDesertList)

            // 3. ListView와 Adapter 연결
            myListView.adapter = recommendedDesertListAdapter

        }
    }

    private fun thisWeekTop3Adapter(inflater: View){
        CoroutineScope(Dispatchers.Main).launch {

            // 1. ListView 객체 생성
            myListView = inflater.findViewById(R.id.rv_most_sell_menu)
            myListView.layoutManager = LinearLayoutManager(ctx,
                LinearLayoutManager.HORIZONTAL, false)
            OverScrollDecoratorHelper.setUpOverScroll(myListView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL)


            // 2. Adapter 객체 생성(한 행을 위해 반복 생성할 Layout과 데이터 전달)
            thisWeekTop3ListAdapter = ThisWeekTop3ListAdapter(ctx, R.layout.item_ready, thisWeekTop3List)

            // 3. ListView와 Adapter 연결
            myListView.adapter = thisWeekTop3ListAdapter

        }
    }

    private fun getRecentOrder(rootview:View){
        val service = MobileCafeApplication.retrofit.create(OrderService::class.java)
        service.selectRecentOrder(LoginActivity.userId).enqueue(object :
            Callback<List<RecentOrderDTO>> {
            override fun onResponse(
                call: Call<List<RecentOrderDTO>>,
                response: Response<List<RecentOrderDTO>>
            ) {
                //정상일 경우 가져옴
                if (response.code() == 200) {
//                    result = response.body()!!
                    orderList = response.body()!!
                    Log.d(TAG, "onResponse: orderlist : {$orderList}")
                    homeAdapter(rootview)

                }
                else {
                    Log.d(TAG, "shoppingplist - onResponse : Error code ${response.code()}")
                }
            }
            override fun onFailure(call: Call<List<RecentOrderDTO>>, t: Throwable) {
                t.printStackTrace()
                Log.d(TAG, "onFailure: 최근 내용 업뎃 오류")
            }
        })
    }

    private fun getNewProduct(rootview:View){
        val service = MobileCafeApplication.retrofit.create(ProductService::class.java)
        service.selectNewProduct().enqueue(object :
            Callback<List<ProductDTO>> {
            override fun onResponse(
                call: Call<List<ProductDTO>>,
                response: Response<List<ProductDTO>>
            ) {
                //정상일 경우 가져옴
                if (response.code() == 200) {
//                    result = response.body()!!
                    newProductList = response.body()!!
                    Log.d(TAG, "onResponse: newProductList : {$newProductList}")
                    newProductAdapter(rootview)

                }
                else {
                    Log.d(TAG, "shoppingplist - onResponse : Error code ${response.code()}")
                }
            }
            override fun onFailure(call: Call<List<ProductDTO>>, t: Throwable) {
                t.printStackTrace()
                Log.d(TAG, "onFailure: 최근 내용 업뎃 오류")
            }
        })
    }

    private fun getRecommendedProduct(rootview:View){
        val service = MobileCafeApplication.retrofit.create(ProductService::class.java)
        service.selectRecommendedProduct().enqueue(object :
            Callback<List<ProductDTO>> {
            override fun onResponse(
                call: Call<List<ProductDTO>>,
                response: Response<List<ProductDTO>>
            ) {
                //정상일 경우 가져옴
                if (response.code() == 200) {
//                    result = response.body()!!
                    recommendedProductList = response.body()!!
                    Log.d(TAG, "onResponse: newProductList : {$recommendedProductList}")
                    recommendedProductAdapter(rootview)

                }
                else {
                    Log.d(TAG, "shoppingplist - onResponse : Error code ${response.code()}")
                }
            }
            override fun onFailure(call: Call<List<ProductDTO>>, t: Throwable) {
                t.printStackTrace()
                Log.d(TAG, "onFailure: 최근 내용 업뎃 오류")
            }
        })
    }

    private fun getRecommendedDesert(rootview:View){
        val service = MobileCafeApplication.retrofit.create(ProductService::class.java)
        service.selectRecommendedDesert().enqueue(object :
            Callback<List<ProductDTO>> {
            override fun onResponse(
                call: Call<List<ProductDTO>>,
                response: Response<List<ProductDTO>>
            ) {
                //정상일 경우 가져옴
                if (response.code() == 200) {
//                    result = response.body()!!
                    recommendedDesertList = response.body()!!
                    Log.d(TAG, "onResponse: newProductList : {$recommendedDesertList}")
                    recommendedDesertAdapter(rootview)

                }
                else {
                    Log.d(TAG, "shoppingplist - onResponse : Error code ${response.code()}")
                }
            }
            override fun onFailure(call: Call<List<ProductDTO>>, t: Throwable) {
                t.printStackTrace()
                Log.d(TAG, "onFailure: 최근 내용 업뎃 오류")
            }
        })
    }

    private fun getTop3Product(rootview:View){
        val service = MobileCafeApplication.retrofit.create(OrderService::class.java)
        service.selectThisWeekOrder().enqueue(object :
            Callback<List<OrderDTOwithTotal>> {
            override fun onResponse(
                call: Call<List<OrderDTOwithTotal>>,
                response: Response<List<OrderDTOwithTotal>>
            ) {
                //정상일 경우 가져옴
                if (response.code() == 200) {
//                    result = response.body()!!
                    thisWeekTop3List = response.body()!!
                    Log.d(TAG, "onResponse: newProductList : {$thisWeekTop3List}")
                    thisWeekTop3Adapter(rootview)

                }
                else {
                    Log.d(TAG, "shoppingplist - onResponse : Error code ${response.code()}")
                }
            }
            override fun onFailure(call: Call<List<OrderDTOwithTotal>>, t: Throwable) {
                t.printStackTrace()
                Log.d(TAG, "onFailure: 최근 내용 업뎃 오류")
            }
        })
    }

}