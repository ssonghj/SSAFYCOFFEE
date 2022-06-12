package com.ssafy.smartcafe.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartcafe.MobileCafeApplication
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.adapter.ProductsListAdapter
import com.ssafy.smartcafe.adapter.SimpleCommentAdapter
import com.ssafy.smartcafe.databinding.ActivityMenuDetailBinding
import com.ssafy.smartcafe.dto.ProductDTO
import com.ssafy.smartcafe.dto.UserDTO
import com.ssafy.smartcafe.service.ProductService
import com.ssafy.smartcafe.service.UserService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "MenuDetailActivity"
class MenuDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuDetailBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var simpleCommentAdapter : SimpleCommentAdapter
    private var productList = arrayListOf<ProductDTO>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var product_id = intent!!.getStringExtra("productId")!!.toInt()

        CoroutineScope(Dispatchers.Main).launch {
            getProductInfo(product_id)
            setData()
        }

        //뒤로가기
        binding.btnBack.setOnClickListener{
            finish()
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
            startActivity(intent)
        }
    }

    private fun setData(){

        var img = productList[0].img
        var resId = this.resources.getIdentifier(
            img.substring(0, img.length - 4),
            "drawable",
            "com.ssafy.smartcafe"
        )
        binding.imgMenu.setImageResource(resId)

        binding.tvMenuName.text = productList[0].name
        binding.tvPrice.text = "${productList[0].price}원"

        binding.tvRating.rating = (productList[0].avg / 2).toFloat()

        binding.tvReviewInfo.text = "${productList[0].commentCnt}건의 리뷰가 있어요!"

        if(productList[0].commentCnt == 0){
            binding.recyclerReview.visibility = View.GONE
        }else{
            setAdapter()
        }
    }

    private fun setAdapter(){
        CoroutineScope(Dispatchers.Main).launch {
            // 1. ListView 객체 생성
            recyclerView = binding.recyclerReview
            recyclerView.layoutManager = LinearLayoutManager(
                applicationContext,
                LinearLayoutManager.VERTICAL, false
            )

            // 2. Adapter 객체 생성(한 행을 위해 반복 생성할 Layout과 데이터 전달)
            simpleCommentAdapter = SimpleCommentAdapter(applicationContext, R.layout.item_simple_review, productList)

            // 3. ListView와 Adapter 연결
            recyclerView.adapter = simpleCommentAdapter
        }
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
}