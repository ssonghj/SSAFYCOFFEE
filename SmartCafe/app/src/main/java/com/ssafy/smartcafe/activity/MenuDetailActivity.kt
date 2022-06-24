package com.ssafy.smartcafe.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelLazy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartcafe.MobileCafeApplication
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.activity.LoginActivity.Companion.detailList
import com.ssafy.smartcafe.activity.LoginActivity.Companion.userId
import com.ssafy.smartcafe.adapter.SimpleCommentAdapter
import com.ssafy.smartcafe.databinding.ActivityMenuDetailBinding
import com.ssafy.smartcafe.dto.*
import com.ssafy.smartcafe.service.OrderService
import com.ssafy.smartcafe.service.ProductService
import com.ssafy.smartcafe.viewModel.MenuDetailViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

private const val TAG = "MenuDetailActivity"
class MenuDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuDetailBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var simpleCommentAdapter : SimpleCommentAdapter
    private var productList = arrayListOf<ProductDTO>()
    private var likeMenuList = arrayListOf<ProductDTO>()
    private var product_id = 0
    private var list = arrayListOf<RecentOrderDTOwithComment>()
    private var d_id = 0

    val mainViewModel: MenuDetailViewModel by ViewModelLazy(
        MenuDetailViewModel::class,
        { viewModelStore },
        { defaultViewModelProviderFactory }
    )

    override fun onResume() {
        super.onResume()
        //리뷰 더보기에서 리뷰 삭제시 반영
        CoroutineScope(Dispatchers.Main).launch {
            getProductInfo(product_id)
            setAdapter()

            getWriteComment()
            //코멘트 쓸거 없으면 버튼 비활성화
            binding.btnReview.visibility = View.GONE
            for(i in list.indices){
                if(list[i].product_id == product_id){
                    //코멘트 쓸거 있으면 버튼 활성화
                    binding.btnReview.visibility = View.VISIBLE
                    d_id = list[i].d_id
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuDetailBinding.inflate(layoutInflater)
        binding.apply {
            lifecycleOwner = this@MenuDetailActivity
            viewModel = mainViewModel
        }

        setContentView(binding.root)

        product_id = intent!!.getStringExtra("productId")!!.toInt()

        CoroutineScope(Dispatchers.Main).launch {
            getProductInfo(product_id)
            getAllUserLikeMenu()//찜한 메뉴
            setData()

            //찜한 메뉴일 경우
            for(i in likeMenuList.indices){
                if(likeMenuList[i].name == productList[0].name){
                    mainViewModel.setLike()
                }
            }
        }


        //뒤로가기
        binding.btnBack.setOnClickListener{
            finish()
        }

        //하트 누르기
        binding.btnLike.setOnClickListener{

            when {
                mainViewModel.getHeartColor() == 0 -> {
                    mainViewModel.setLike()
                    //서버에 좋아하는 메뉴에 추가
                    CoroutineScope(Dispatchers.Main).launch {
                        InsertUserLikeMenu();
                    }
                }
                else -> {
                    mainViewModel.setNotLike()
                    //서버에 좋아하는 메뉴에서 삭제
                    CoroutineScope(Dispatchers.Main).launch {
                        DeleteUserLikeMenu();
                    }
                }
            }
        }


        CoroutineScope(Dispatchers.Main).launch {
            getWriteComment()

            //코멘트 쓸거 없으면 버튼 비활성화
            binding.btnReview.visibility = View.GONE
            for(i in list.indices){
                if(list[i].product_id == product_id){
                    //코멘트 쓸거 있으면 버튼 활성화
                    binding.btnReview.visibility = View.VISIBLE
                    d_id = list[i].d_id
                }
            }
        }

        //리뷰쓰기 버튼
        binding.btnReview.setOnClickListener{
            val intent = Intent(applicationContext, WriteReviewActivity::class.java)
            intent.putExtra("menuName", productList[0].name)
            intent.putExtra("productId",product_id)
            intent.putExtra("d_id", d_id)
            startActivity(intent)
        }

        //수량 증가
        binding.tvPlus.setOnClickListener{
            var txt = binding.tvCount.text
            var num = Integer.parseInt(txt.toString())
            binding.tvCount.text = (num+1).toString()
        }
        //수량 감소
        binding.tvMinus.setOnClickListener{
            var txt = binding.tvCount.text
            var num = Integer.parseInt(txt.toString())
            if(num > 1){
                binding.tvCount.text = (num-1).toString()
            }
        }

        //리뷰 더보기
        binding.tvMoreDetailReview.setOnClickListener{
            val intent = Intent(this, MenuReviewActivity::class.java)
            intent.putExtra("product_id",product_id.toString())
            print("product_name : ${productList[0].name}")
            intent.putExtra("product_name", productList[0].name)
            startActivity(intent)
        }

        //메뉴 담기
        binding.frameOrderBtn.setOnClickListener{

            //메뉴 수량
            var quantity = binding.tvCount.text.toString().toInt()
            var details = OrderDetailDTO(0,0,product_id,quantity)

            detailList.add(details)

            finish()
        }
    }

    private fun setData(){

        //이미지는 아직 viewmodel로 못바꿈
        var img = productList[0].img
        var resId = this.resources.getIdentifier(
            img.substring(0, img.length - 4),
            "drawable",
            "com.ssafy.smartcafe"
        )
        binding.imgMenu.setImageResource(resId)

//        mainViewModel.setImg(img)

        mainViewModel.setName(productList[0].name)
        mainViewModel.setPrice(productList[0].price)
        mainViewModel.setRating(productList[0].avg/2)
        mainViewModel.setReviewInfo(productList[0].commentCnt)

        if(productList[0].commentCnt == 0){
            binding.recyclerReview.visibility = View.GONE
        }else{
            setAdapter()
        }
    }

    private fun setAdapter(){
        // 1. ListView 객체 생성
        recyclerView = binding.recyclerReview
        recyclerView.layoutManager = LinearLayoutManager(
            applicationContext,
            LinearLayoutManager.VERTICAL, false
        )
        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL)

        // 2. Adapter 객체 생성(한 행을 위해 반복 생성할 Layout과 데이터 전달)
        simpleCommentAdapter = SimpleCommentAdapter(applicationContext, R.layout.item_simple_review, productList)

        // 3. ListView와 Adapter 연결
        recyclerView.adapter = simpleCommentAdapter
    }

    private suspend fun getProductInfo(id:Int) {
        withContext(Dispatchers.IO) {
            val service = MobileCafeApplication.retrofit.create(ProductService::class.java)
            val response = service.selectProduct(id).execute()

            if (response.code() == 200) {
                var res = response.body()!!
                productList = res as ArrayList<ProductDTO>

                println("MenuDetailActivity : ${res}")
            } else {
                Log.d(TAG, "MenuDetailActivity: error code")
            }
        }
    }

    private suspend fun getAllUserLikeMenu() {
        withContext(Dispatchers.IO) {
            val service = MobileCafeApplication.retrofit.create(ProductService::class.java)
            val response = service.selectUserLikeMenu(LoginActivity.userId).execute()

            if (response.code() == 200) {
                likeMenuList = (response.body() as ArrayList<ProductDTO>?)!!

                println("getAllUserLikeMenu : ${productList}")
            } else {
                println("getAllUserLikeMenu: error code")
            }
        }
    }

    private suspend fun InsertUserLikeMenu() {
        var userLikeDTO = UserLikeDTO(0,userId,product_id)

        withContext(Dispatchers.IO) {
            val service = MobileCafeApplication.retrofit.create(ProductService::class.java)
            val response = service.insertLikeMenu(userLikeDTO)

            if (response.code() == 200) {
                var res = response.body()!!
                println("InsertUserLikeMenu : ${res}")
            } else {
                Log.d(TAG, "InsertUserLikeMenu: error code")
            }
        }
    }

    private suspend fun DeleteUserLikeMenu() {
        var userLikeDTO = UserLikeDTO(0,userId,product_id)

        withContext(Dispatchers.IO) {
            val service = MobileCafeApplication.retrofit.create(ProductService::class.java)
            val response = service.removeLikeMenu(userLikeDTO)

            if (response.code() == 200) {
                var res = response.body()!!
                println("DeleteUserLikeMenu : ${res}")
            } else {
                Log.d(TAG, "DeleteUserLikeMenu: error code")
            }
        }
    }

    private suspend fun getWriteComment(){
        withContext(Dispatchers.IO) {
            val service = MobileCafeApplication.retrofit.create(OrderService::class.java)
            val response = service.selectWriteComment(userId).execute()

            if (response.code() == 200) {
                list = (response.body() as ArrayList<RecentOrderDTOwithComment>?)!!
                println("getWriteComment : ${list}")
            } else {
                Log.d(TAG, "getWriteComment: error code")
            }
        }
    }
}