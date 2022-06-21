package com.ssafy.smartcafe.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartcafe.MobileCafeApplication
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.activity.*
import com.ssafy.smartcafe.activity.LoginActivity.Companion.detailList
import com.ssafy.smartcafe.activity.LoginActivity.Companion.userId
import com.ssafy.smartcafe.activity.LoginActivity.Companion.userName
import com.ssafy.smartcafe.activity.LoginActivity.Companion.userStamp
import com.ssafy.smartcafe.adapter.ProductsListAdapter
import com.ssafy.smartcafe.adapter.StampAdapter
import com.ssafy.smartcafe.databinding.FragmentMypageBinding
import com.ssafy.smartcafe.dto.OrderDetailDTO
import com.ssafy.smartcafe.dto.ProductDTO
import com.ssafy.smartcafe.service.ProductService
import com.ssafy.smartcafe.service.UserService
import com.ssafy.smartcafe.util.getLevel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper


private const val TAG = "MypageFragment"
class MypageFragment : Fragment() {
    private lateinit var ctx: Context
    private lateinit var binding: FragmentMypageBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var stampAdapter : StampAdapter

    private var stampList = mutableListOf<String>()
    private var stampCnt = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //context받아옴
        ctx = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageBinding.inflate(layoutInflater)

        binding.tvUserName.text = LoginActivity.userName+"님"

        setLevel()

        //준비중인 메뉴 확인
        binding.tvSeeDetail.setOnClickListener{
            val intent = Intent(ctx, DetailCurOrderMenuActivity::class.java)
            startActivity(intent)
        }

        //리뷰 관리
        binding.frameReview.setOnClickListener{
            val intent = Intent(ctx, UserReviewActivity::class.java)
            startActivity(intent)
        }

        //주문내역
        binding.frameOrderList.setOnClickListener{
            val intent = Intent(ctx, UserRecentOrderListActivity::class.java)
            startActivity(intent)
        }

        binding.frameUserLike.setOnClickListener{
            val intent = Intent(ctx, UserLikeActivity::class.java)
            startActivity(intent)
        }

        //알림 내역
        binding.btnNotification.setOnClickListener{
            val intent = Intent(ctx, NotificationActivity::class.java)
            startActivity(intent)
        }

        //설정
        binding.btnSetting.setOnClickListener{
            val intent = Intent(ctx, SettingActivity::class.java)
            startActivity(intent)
        }

        //로그아웃
        binding.frameLogout.setOnClickListener {
            //유저 데이터 초기화
            userId = ""
            userName = ""
            userStamp = 0
            detailList = arrayListOf()

            var intent = Intent(ctx, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        return binding.root
    }

    private fun setLevel() {
        CoroutineScope(Dispatchers.Main).launch {
            getUserInfo()

            val level = getLevel(stampCnt)
            binding.tvLevel.text = level[0] // 브론즈 0
            binding.tvNextLevelNoti.text = level[2]

            println("level 1 : ${level[1]}")
            for(i in 0 until level[1].toInt()){
                stampList.add("icon.png")
            }
            for(i in level[1].toInt() until 10){
                stampList.add("oval.png")
            }

            setAdapter()
        }
    }

    private fun setAdapter(){

        // 1. ListView 객체 생성
        recyclerView = binding.recyclerCoupon
        recyclerView.layoutManager = GridLayoutManager(ctx,5)
        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL)

        // 2. Adapter 객체 생성(한 행을 위해 반복 생성할 Layout과 데이터 전달)
        stampAdapter = StampAdapter(ctx, R.layout.item_no_coupon, stampList)

        // 3. ListView와 Adapter 연결
        recyclerView.adapter = stampAdapter
    }

    private suspend fun getUserInfo() {
        withContext(Dispatchers.IO) {
            val service = MobileCafeApplication.retrofit.create(UserService::class.java)
            val response = service.getSimpleUserInfo(userId).execute()

            if (response.code() == 200) {
                var user = response.body()!!
                stampCnt = user.stamps
                println("getUserInfo : ${stampCnt}")
            } else {
                Log.d(TAG, "getUserInfo: error code")
            }
        }
    }

}