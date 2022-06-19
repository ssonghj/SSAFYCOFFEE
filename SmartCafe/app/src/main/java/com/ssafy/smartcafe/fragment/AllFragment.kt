package com.ssafy.smartcafe.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartcafe.MobileCafeApplication
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.adapter.ProductsListAdapter
import com.ssafy.smartcafe.databinding.FragmentAllBinding
import com.ssafy.smartcafe.dto.ProductDTO
import com.ssafy.smartcafe.dto.UserDTO
import com.ssafy.smartcafe.service.ProductService
import com.ssafy.smartcafe.service.UserService
import com.ssafy.smartcafe.viewModel.AllFragmentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

private const val TAG = "AllFragment"
class AllFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var allFragmentAdapter : ProductsListAdapter
    private lateinit var productList:List<ProductDTO>


    private lateinit var binding:FragmentAllBinding
    private lateinit var ctx:Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAllBinding.inflate(layoutInflater)

        //스피너 설정 함수
        setSpinner()


        return binding.root
    }

    private fun setSpinner(){
        val spinnerItem = resources.getStringArray(R.array.my_array)
        val spinnerAdapter = ArrayAdapter(ctx, R.layout.item_spinner, spinnerItem)
        binding.spinner.adapter = spinnerAdapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                CoroutineScope(Dispatchers.Main).launch {

                    //아이템이 클릭 되면 맨 위부터 position 0번부터 순서대로 동작하게 됩니다.
                    when (position) {
                        //인기순
                        0 -> {
                            getProductWithSell()
                        }
                        //낮은 가격순
                        1 -> {
                            getProductWithRowPrice()
                        }
                        //높은가격순
                        2 -> {
                            getProductWithHighPrice()
                        }
                        //리뷰많은순
                        3 -> {
                            getProductWithHighCommentCnt()
                        }
                        //평점순
                        else -> {
                            getProductWithHighRating()
                        }
                    }
                    setAdapter()
                }
                //끝난 후에는 ui 갱신
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                CoroutineScope(Dispatchers.Main).launch {
                    getProductWithSell()
                    setAdapter()
                }
            }
        }
    }


    private fun setAdapter(){

        CoroutineScope(Dispatchers.Main).launch {

            // 1. ListView 객체 생성
            recyclerView = binding.recyclerAllMenu
            recyclerView.layoutManager = GridLayoutManager(ctx,3)
            OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL)

            // 2. Adapter 객체 생성(한 행을 위해 반복 생성할 Layout과 데이터 전달)
            allFragmentAdapter = ProductsListAdapter(ctx, R.layout.item_menu, productList)

            // 3. ListView와 Adapter 연결
            recyclerView.adapter = allFragmentAdapter

        }
    }


    private suspend fun getProductWithSell() {
        withContext(Dispatchers.IO) {
            val service = MobileCafeApplication.retrofit.create(ProductService::class.java)
            val response = service.selectProductWithSell().execute()

            if (response.code() == 200) {
                productList = (response.body() as ArrayList<ProductDTO>?)!!
                println("getProductWithSell : ${productList}")
            } else {
                Log.d(TAG, "getProductWithSell: error code")
            }
        }
    }

    private suspend fun getProductWithRowPrice() {
        withContext(Dispatchers.IO) {
            val service = MobileCafeApplication.retrofit.create(ProductService::class.java)
            val response = service.selectProductWithRowPrice().execute()

            if (response.code() == 200) {
                productList = (response.body() as ArrayList<ProductDTO>?)!!
                println("getProductWithSell : ${productList}")
            } else {
                Log.d(TAG, "getProductWithSell: error code")
            }
        }
    }

    private suspend fun getProductWithHighPrice() {
        withContext(Dispatchers.IO) {
            val service = MobileCafeApplication.retrofit.create(ProductService::class.java)
            val response = service.selectProductWithHighPrice().execute()

            if (response.code() == 200) {
                productList = (response.body() as ArrayList<ProductDTO>?)!!
                println("getProductWithSell : ${productList}")
            } else {
                Log.d(TAG, "getProductWithSell: error code")
            }
        }
    }

    private suspend fun getProductWithHighCommentCnt() {
        withContext(Dispatchers.IO) {
            val service = MobileCafeApplication.retrofit.create(ProductService::class.java)
            val response = service.selectProductWithHighCommentCnt().execute()

            if (response.code() == 200) {
                productList = (response.body() as ArrayList<ProductDTO>?)!!
                println("getProductWithSell : ${productList}")
            } else {
                Log.d(TAG, "getProductWithSell: error code")
            }
        }
    }

    private suspend fun getProductWithHighRating() {
        withContext(Dispatchers.IO) {
            val service = MobileCafeApplication.retrofit.create(ProductService::class.java)
            val response = service.selectProductWithHighRating().execute()

            if (response.code() == 200) {
                productList = (response.body() as ArrayList<ProductDTO>?)!!
                println("getProductWithSell : ${productList}")
            } else {
                Log.d(TAG, "getProductWithSell: error code")
            }
        }
    }
}