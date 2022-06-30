package com.ssafy.smartcafe.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartcafe.MobileCafeApplication
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.adapter.ProductsListAdapter
import com.ssafy.smartcafe.databinding.FragmentCategoryBinding
import com.ssafy.smartcafe.dto.ProductDTO
import com.ssafy.smartcafe.service.ProductService
import com.ssafy.smartcafe.viewModel.AllFragmentViewModel
import com.ssafy.smartcafe.viewModel.JoinViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper


private const val TAG = "CategoryFragment"
class CategoryFragment : Fragment() {
    private lateinit var binding:FragmentCategoryBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryFragmentAdapter : ProductsListAdapter
    private lateinit var productList:List<ProductDTO>

    val mainViewModel: AllFragmentViewModel by ViewModelLazy(
        AllFragmentViewModel::class,
        { viewModelStore },
        { defaultViewModelProviderFactory }
    )

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
        binding.apply {
            lifecycleOwner = this@CategoryFragment
            viewModel = mainViewModel
        }

        CoroutineScope(Dispatchers.Main).launch {
            getAllCoffee()
            setAdapter()
        }

        binding.tvDrink.setOnClickListener{
            //클릭해도 아직 색상 안바뀜 -> 뷰모델 설정해서 post로 바꾸어주어야 할 것 같음.
            CoroutineScope(Dispatchers.Main).launch {
                mainViewModel.changeDrink()
                getAllCoffee()
                setAdapter()
                binding.btnSearch.setQuery("", false);
            }
        }

        binding.tvDesert.setOnClickListener{
            CoroutineScope(Dispatchers.Main).launch {
                mainViewModel.changeDesert()
                getAllDesert()
                setAdapter()
                binding.btnSearch.setQuery("", false);
            }
        }

        var searchViewTextListener: SearchView.OnQueryTextListener =
            object : SearchView.OnQueryTextListener {
                //검색버튼 입력시 호출, 검색버튼이 없으므로 사용하지 않음
                override fun onQueryTextSubmit(s: String): Boolean {
                    return false
                }

                //텍스트 입력/수정시에 호출
                override fun onQueryTextChange(s: String): Boolean {
                    categoryFragmentAdapter.filter.filter(s)

                    Log.d(TAG, "SearchVies Text is changed : $s")
                    return false
                }

            }

        binding.btnSearch.setOnQueryTextListener(searchViewTextListener)
        return binding.root
    }

    private fun setAdapter(){

        CoroutineScope(Dispatchers.Main).launch {

            // 1. ListView 객체 생성
            recyclerView = binding.recyclerCategoryMenu
            recyclerView.layoutManager = GridLayoutManager(ctx,3)
            OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL)


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

