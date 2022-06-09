package com.ssafy.smartcafe.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import com.shashank.sony.fancytoastlib.FancyToast
import com.ssafy.smartcafe.MobileCafeApplication
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.databinding.ActivityJoinBinding
import com.ssafy.smartcafe.service.UserService
import com.ssafy.smartcafe.viewModel.JoinViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "JoinActivity"
class JoinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJoinBinding
    val mainViewModel: JoinViewModel by ViewModelLazy(
        JoinViewModel::class,
        { viewModelStore },
        { defaultViewModelProviderFactory }
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_join)
        binding.apply {
            lifecycleOwner = this@JoinActivity
            viewModel = mainViewModel
        }

        binding.ivBack.setOnClickListener {
            finish()
        }

        //중복체크 버튼
        binding.frameCheckBtn.setOnClickListener{
            val inputId = binding.etId.text.toString()

            CoroutineScope(Dispatchers.Main).launch {
                checkId(inputId)
            }
        }
    }

    private suspend fun checkId(inputId:String) {
        println("inputid : ${inputId}")

        withContext(Dispatchers.IO){
            val service = MobileCafeApplication.retrofit.create(UserService::class.java)
            val response = service.checkUser(inputId).execute()

            if(response.code() == 200){

                var res = response.body()!!
                Log.d(TAG, "getIDPass: ${res}")

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
}