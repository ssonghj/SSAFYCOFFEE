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
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.ssafy.smartcafe.MobileCafeApplication
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.activity.*
import com.ssafy.smartcafe.activity.LoginActivity.Companion.detailList
import com.ssafy.smartcafe.activity.LoginActivity.Companion.userId
import com.ssafy.smartcafe.activity.LoginActivity.Companion.userName
import com.ssafy.smartcafe.activity.LoginActivity.Companion.userStamp
import com.ssafy.smartcafe.adapter.*
import com.ssafy.smartcafe.databinding.FragmentMypageBinding
import com.ssafy.smartcafe.dto.OrderDetailDTO
import com.ssafy.smartcafe.dto.ProductDTO
import com.ssafy.smartcafe.dto.RecentOrderDTO
import com.ssafy.smartcafe.service.OrderService
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
    private lateinit var curOrderAdapter : MypageCurOrderAdapter

    private var productList = arrayListOf<RecentOrderDTO>()
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

            //카카오 로그인일 경우
            if(LoginActivity.loginInfo=="kakaoLogin") {
                UserApiClient.instance.logout { error ->
                    if (error != null) {
                        Log.d(TAG, "onCreateView: 로그아웃 실패")
                    } else {
                        Log.d(TAG, "onCreateView: 로그아웃 성공")
                        //유저 데이터 초기화
                        userId = ""
                        userName = ""
                        userStamp = 0
                        detailList = arrayListOf()
                        LoginActivity.loginInfo==""

                        var intent = Intent(ctx, LoginActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                    }
                }
            }

            //네이버 로그인일 경우
            else if(LoginActivity.loginInfo=="naverLogin"){
                userId = ""
                userName = ""
                userStamp = 0
                detailList = arrayListOf()
                LoginActivity.loginInfo==""
                NidOAuthLogin().callDeleteTokenApi(ctx, object : OAuthLoginCallback {
                    override fun onSuccess() {
                        //Toast.makeText(this@MainActivity, "네이버 아이디 토큰삭제 성공!", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "onSuccess: 토큰삭제")
                    }
                    override fun onFailure(httpStatus: Int, message: String) {
                        // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                        // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
                        Log.d("naver", "errorCode: ${NaverIdLoginSDK.getLastErrorCode().code}")
                        Log.d("naver", "errorDesc: ${NaverIdLoginSDK.getLastErrorDescription()}")
                    }
                    override fun onError(errorCode: Int, message: String) {
                        // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                        // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
                        onFailure(errorCode, message)
                    }
                })
                var intent = Intent(ctx, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }

            else if(LoginActivity.loginInfo=="googleLogin") {
                userId = ""
                userName = ""
                userStamp = 0
                detailList = arrayListOf()
                LoginActivity.loginInfo == ""
                Firebase.auth.signOut()

                val opt = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                val client = GoogleSignIn.getClient(ctx, opt)
                client.signOut()

                var intent = Intent(ctx, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }

            else{
                //유저 데이터 초기화
                userId = ""
                userName = ""
                userStamp = 0
                detailList = arrayListOf()

                var intent = Intent(ctx, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }


        }

        //주문중인 메뉴들 불러오기
        CoroutineScope(Dispatchers.Main).launch {
            getCurOrder()
            setAdapteOfCurOrder()
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

    private fun setAdapteOfCurOrder(){

        // 1. ListView 객체 생성
        recyclerView = binding.recyclerWaitingMenu
        recyclerView.layoutManager = LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false)
        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL)

        // 2. Adapter 객체 생성(한 행을 위해 반복 생성할 Layout과 데이터 전달)
        curOrderAdapter = MypageCurOrderAdapter(ctx, R.layout.item_ready, productList)

        // 3. ListView와 Adapter 연결
        recyclerView.adapter = curOrderAdapter
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

    private suspend fun getCurOrder() {
        withContext(Dispatchers.IO) {
            val service = MobileCafeApplication.retrofit.create(OrderService::class.java)
            val response = service.selectCurOrder(userId).execute()

            if (response.code() == 200) {
                productList = (response.body() as ArrayList<RecentOrderDTO>?)!!
                println("getCurOrder : ${productList}")
            } else {
                println("getCurOrder: error code")
            }
        }
    }

}