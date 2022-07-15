package com.ssafy.smartcafe.activity

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.*
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import com.shashank.sony.fancytoastlib.FancyToast
import com.ssafy.smartcafe.MobileCafeApplication
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.activity.LoginActivity.Companion.vibratePhone
import com.ssafy.smartcafe.databinding.ActivityJoinBinding
import com.ssafy.smartcafe.dto.UserDTO
import com.ssafy.smartcafe.service.UserService
import com.ssafy.smartcafe.viewModel.JoinViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "JoinActivity"

class JoinActivity : AppCompatActivity() {

    companion object{
        lateinit var ctx: JoinActivity
    }

    private lateinit var binding: ActivityJoinBinding
    private val mainViewModel: JoinViewModel by ViewModelLazy(
        JoinViewModel::class,
        { viewModelStore },
        { defaultViewModelProviderFactory }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ctx = this
        binding = DataBindingUtil.setContentView(this, R.layout.activity_join)
        binding.apply {
            lifecycleOwner = this@JoinActivity
            viewModel = mainViewModel
        }

        binding.ivBack.setOnClickListener {
            vibratePhone(application)
            finish()
        }

        //중복체크 버튼
        binding.frameCheckBtn.setOnClickListener{

            vibratePhone(application)

            val inputId = binding.etId.text.toString()

            if(inputId == ""){
                FancyToast.makeText(applicationContext,
                    "아이디를 입력해주세요",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.CONFUSING,
                    false).show()
            }
            else{
                CoroutineScope(Dispatchers.Main).launch {
                    checkId(inputId)
                }
            }
        }

        binding.frameJoinBtn.setOnClickListener{

            //버튼 클릭시 진동 (전역변수) -> 모든 버튼에 적용
            vibratePhone(application)

            val id = binding.etId.text.toString()
            val pass = binding.etPw.text.toString()
            val passCheck = binding.etPwCheck.text.toString()
            val nickname = binding.etNickname.text.toString()

            //칸이 비었을 경우
            if(id == "" || pass == "" || passCheck == "" || nickname == ""){
                FancyToast.makeText(applicationContext,
                    "빈칸을 채워주세요",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.CONFUSING,
                    false).show()
            }
            //중복체크 안되어있을 경우
            else if(mainViewModel.getColorValue().value == 0){
                FancyToast.makeText(applicationContext,
                    "아이디 중복체크 해주세요",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.CONFUSING,
                    false).show()
            }
            //비밀번호와 비번 체크 동일하지 않은 경우
            else if(pass != passCheck){
                FancyToast.makeText(applicationContext,
                    "비밀번호가 동일하지 않습니다.",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.CONFUSING,
                    false).show()
            }
            //정상 회원가입
            else{ //서버에 회원넣기 로직

                val user = UserDTO(id,nickname,pass,0)

                CoroutineScope(Dispatchers.Main).launch {
                    addUser(user)
                }

                FancyToast.makeText(applicationContext,
                    "회원가입이 완료되었습니다.",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.SUCCESS,
                    false).show()

                finish()
            }
        }
    }

    suspend fun checkId(inputId:String) {
        println("inputid : ${inputId}")

        withContext(Dispatchers.IO){
            val service = MobileCafeApplication.retrofit.create(UserService::class.java)
            val response = service.checkUser(inputId).execute()

            if(response.code() == 200){

                var res = response.body()!!

                //로그인할 아이디가 이미 사용중인 경우 토스트 메시지
                if(res){
                    mainViewModel.setUsed()
                }
                //로그인아이디 사용되지 않은 경우
                else{
                    mainViewModel.setCanUsed()
                }

                //뷰모델 업데이트
                binding.setLifecycleOwner(this@JoinActivity)

            }else{
                Log.d(TAG, "getIDPass: error code")
            }
        }
    }

    private suspend fun addUser(user: UserDTO) {
        withContext(Dispatchers.IO) {
            val service = MobileCafeApplication.retrofit.create(UserService::class.java)
            val response = service.userInsert(user).execute()

            if (response.code() == 200) {
                var res = response.body()!!
                println("joinSuccess : ${res}")
            } else {
                Log.d(TAG, "joinSuccess: error code")
            }
        }
    }

}