package com.ssafy.smartcafe.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartcafe.MobileCafeApplication
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.adapter.ProductsListAdapter
import com.ssafy.smartcafe.databinding.FragmentCategoryBinding
import com.ssafy.smartcafe.dto.ProductDTO
import com.ssafy.smartcafe.service.ProductService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "CategoryFragment"
class CategoryFragment : Fragment() {
    private lateinit var binding:FragmentCategoryBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryFragmentAdapter : ProductsListAdapter
    private lateinit var productList:List<ProductDTO>

    private lateinit var ctx: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryBinding.inflate(layoutInflater)

        CoroutineScope(Dispatchers.Main).launch {
            getAllCoffee()
            setAdapter()
        }

        binding.tvDrink.setOnClickListener{
            binding.tvDrink.currentTextColor.apply {
                R.color.button_color_seventy
            }
            binding.tvDesert.currentTextColor.apply {
                R.color.login_user_color
            }
            CoroutineScope(Dispatchers.Main).launch {
                getAllCoffee()
                setAdapter()
            }
        }

        binding.tvDesert.setOnClickListener{
            binding.tvDrink.currentTextColor.apply {
                R.color.login_user_color
            }
            binding.tvDesert.currentTextColor.apply {
                R.color.button_color_seventy
            }
            CoroutineScope(Dispatchers.Main).launch {
                getAllDesert()
                setAdapter()
            }
        }


        return binding.root
    }

    private fun setAdapter(){

        CoroutineScope(Dispatchers.Main).launch {

            // 1. ListView 객체 생성
            recyclerView = binding.recyclerCategoryMenu
            recyclerView.layoutManager = GridLayoutManager(ctx,3)

            // 2. Adapter 객체 생성(한 행을 위해 반복 생성할 Layout과 데이터 전달)
            categoryFragmentAdapter = ProductsListAdapter(ctx, R.layout.item_menu, productList)

            // 3. ListView와 Adapter 연결
            recyclerView.adapter = categoryFragmentAdapter

        }
    }

    private suspend fun getAllCoffee() {
        withContext(Dispatchers.IO) {
            val service = MobileCafeApplication.retrofit.create(ProductService::class.java)
            val response = service.selectAllCoffee().execute()

            if (response.code() == 200) {
                productList = (response.body() as ArrayList<ProductDTO>?)!!
                println("getAllCoffee : ${productList}")
            } else {
                Log.d(TAG, "getAllCoffee: error code")
            }
        }
    }

    private suspend fun getAllDesert() {
        withContext(Dispatchers.IO) {
            val service = MobileCafeApplication.retrofit.create(ProductService::class.java)
            val response = service.selectAllDesert().execute()

            if (response.code() == 200) {
                productList = (response.body() as ArrayList<ProductDTO>?)!!
                println("getAllDesert : ${productList}")
            } else {
                Log.d(TAG, "getAllDesert: error code")
            }
        }
    }
}

