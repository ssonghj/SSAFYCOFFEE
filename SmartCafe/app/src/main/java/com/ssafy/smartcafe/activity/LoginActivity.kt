package com.ssafy.smartcafe.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.shashank.sony.fancytoastlib.FancyToast
import com.ssafy.smartcafe.MobileCafeApplication
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.databinding.ActivityLoginBinding
import com.ssafy.smartcafe.dto.OrderDTO
import com.ssafy.smartcafe.dto.OrderDetailDTO
import com.ssafy.smartcafe.dto.UserDTO
import com.ssafy.smartcafe.service.UserService
import kotlinx.coroutines.*
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.ssafy.smartcafe.activity.JoinActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val TAG = "LoginActivity"
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var user:UserDTO
    private var idCheck = false

    private lateinit var api_id: String
    private lateinit var api_nickName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //키 해시 알아내는 코드(등록 후 삭제!)
//        var keyHash = Utility.getKeyHash(this)
//        Log.d(TAG, "onCreate: 키 해시 : $keyHash")


        binding.frameLoginBtn.setOnClickListener{
            val id = binding.editId.text.toString()
            val pass = binding.editPass.text.toString()

            CoroutineScope(Dispatchers.Main).launch {
                var tmpUser = UserDTO(id,"",pass,0)
                getIDPass(tmpUser, id, pass)
            }
        }

        binding.tvJoin.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY))
        }
// 로그인 정보 확인
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Toast.makeText(this, "토큰 정보 보기 실패", Toast.LENGTH_SHORT).show()
            }
            else if (tokenInfo != null) {
                Toast.makeText(this, "토큰 정보 보기 성공", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
        }
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                        Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                        Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                        Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                        Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                        Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                        Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.ServerError.toString() -> {
                        Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                        Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                    }
                    else -> { // Unknown
                        Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else if (token != null) {

                //Toast.makeText(this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                    UserApiClient.instance.me { user, error ->
                        if (user != null) {
                            api_id = user?.kakaoAccount?.email.toString()
                            api_nickName = user?.kakaoAccount?.profile?.nickname.toString()
                            var pass = user?.id.toString()
                            //Log.d(TAG, "onCreate:  비밀번호: $pass")
                            CoroutineScope(Dispatchers.Main).launch {
//                                JoinActivity.ctx.checkId(id)
//                                //autoLogin(id,pass)
                                checkId(api_id,api_nickName,pass)

                                Log.d(TAG, "onCreate: 로그인 후아이디 중복 체크")
                            }


                        }
                    }

                LoginActivity.userId = api_id
                LoginActivity.userName = api_nickName
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY))
                finish()
            }
        }
        binding.frameKakao.setOnClickListener {
            Log.d(TAG, "onCreate: 카카오로그인 클릭")
            //kakaoLogin()
            if(LoginClient.instance.isKakaoTalkLoginAvailable(this)){
                LoginClient.instance.loginWithKakaoTalk(this, callback = callback)
            }else{
                LoginClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
    }

    //아이디 중복체크
    private fun checkId(id: String,nickName:String, pw:String):Boolean{

        var answer = false
        Log.d(TAG, "checkId: 중복체크하는 아이디: $id")

        //토스트만 지우면 됌.
        CoroutineScope(Dispatchers.Main).launch {
            //회원 테이블에서 같은 아이디가 있는지 확인한다
            val service = MobileCafeApplication.retrofit.create(UserService::class.java)
            service.checkUser(id).enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if (response.code() == 200) {
                        val res = response.body()!!
                        if(!res){
                            //Toast.makeText(this@LoginActivity,"사용 가능한 아이디입니다.",Toast.LENGTH_SHORT).show()
                            idCheck=true
                            answer = true
                            Log.d(TAG, "onResponse: 사용가능한 아이디 $id")
                            joinServer(id,nickName,pw)
                        }
                        else{
                            //Toast.makeText(this@LoginActivity, "중복되는 아이디입니다.",Toast.LENGTH_SHORT).show()
                            idCheck=false
                            answer = false
                        }
                    }else{
                        Log.d(TAG, "onResponse: Error Code ${response.code()}")
                    }
                }
                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    t.printStackTrace()
                    Log.d(TAG, "onFailure: 아이디 중복 확인 오류")
                }

            }
            )
        }
        Log.d(TAG, "checkId: 중복체크 결과 ${answer}")
        return answer
    }

    //아이디 회원가입
    private fun joinServer(id:String, newNickname:String,pw:String){
        Log.d(TAG, "joinServer: 회원가입 id: $id")
        CoroutineScope(Dispatchers.Main).launch {
            //MobileCafeRepository.get().userInsert(UserDTO(newId, newNickname,newPass,0))
            val service = MobileCafeApplication.retrofit.create(UserService::class.java)
            service.userInsert(UserDTO(id, newNickname, pw, 0))
                .enqueue(object : Callback<Unit> {
                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                        //정상일 경우 가져옴
                        if (response.code() == 200) {
                            var check = response.body()!!
                            Log.d(TAG, "onResponse: ${check}")
                            Toast.makeText(
                                this@LoginActivity,
                                "가입 성공.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Log.d(TAG, "가입 오류 - onResponse : Error code ${response.code()}")
                        }

                    }

                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                        t.printStackTrace()
                        Log.d(TAG, "onFailure:  가입 오류")

                    }

                })
        }
    }

    private suspend fun getIDPass(inputUser:UserDTO, id:String, pass:String) {

        withContext(Dispatchers.IO){
            val service = MobileCafeApplication.retrofit.create(UserService::class.java)
            val response = service.userLogin(inputUser).execute()

            if(response.code() == 200){

                var res = response.body()

                //로그인할 아이디와 비번 값이 없는 경우 토스트 메시지
                if(res == null){
                    val handler = Handler(Looper.getMainLooper())
                    handler.postDelayed(java.lang.Runnable {
                        FancyToast.makeText(applicationContext,
                            "아이디 혹은 비밀번호가 일치하지 않습니다.",
                            FancyToast.LENGTH_SHORT,
                            FancyToast.CONFUSING,
                            false).show()
                    }, 0)
                }
                //로그인아이디와 비번 있을 경우
                else{
                    user = res!!
                    if(user.id == id && user.pass == pass){
                        LoginActivity.userId = user.id
                        LoginActivity.userName = user.name
                        LoginActivity.userStamp = user.stamps
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                    }
                }

                Log.d(TAG, "getIDPass: ${res}")
            }else{
                Log.d(TAG, "getIDPass: error code")
            }
        }

    }

    //미등록 회원일경우 여기서 회원가입을 해줘야하는데 같은 로직 두번 쓰지 않을 방법이 있을까 생각해보는 중,,,,,,
//    private fun kakaoLogin(){
//        //카카오톡(카카오계정)로그인
//        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
//            if (error != null) {
//                //Toast.makeText(this, "토큰 정보 보기 실패", Toast.LENGTH_SHORT).show()
//            } else if (tokenInfo != null) {
//                //Toast.makeText(this, "토큰 정보 보기 성공", Toast.LENGTH_SHORT).show()
////                val intent = Intent(this, MainActivity::class.java)
////                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
////                finish()
//                UserApiClient.instance.me { user, error ->
//                    var id = user?.kakaoAccount?.email.toString()
//                    var pass = user?.id.toString()
//                    //Log.d(TAG, "onCreate:  비밀번호: $pass")
//                    //autoLogin(id, pass)
//                    //checkId(id)
//                }
//            }
//        }
//
//        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
//            if (error != null) {
//                when {
//                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
//                        Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
//                    }
//                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
//                        Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
//                    }
//                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
//                        Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT)
//                            .show()
//                    }
//                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
//                        Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
//                    }
//                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
//                        Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
//                    }
//                    error.toString() == AuthErrorCause.Misconfigured.toString() -> {
//                        Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT)
//                            .show()
//                    }
//                    error.toString() == AuthErrorCause.ServerError.toString() -> {
//                        Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
//                    }
//                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
//                        Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
//                    }
//                    else -> { // Unknown
//                        Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            } else if (token != null) {
//
//                //Toast.makeText(this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()
//                CoroutineScope(Dispatchers.Main).launch {
//                    UserApiClient.instance.me { user, error ->
//                        if (user != null) {
//                            var id = user?.kakaoAccount?.email.toString()
//                            var nickName = user?.kakaoAccount?.profile?.nickname.toString()
//                            var pass = user?.id.toString()
//                            //Log.d(TAG, "onCreate:  비밀번호: $pass")
//                            CoroutineScope(Dispatchers.Main).launch {
//                                JoinActivity.ctx.checkId(id)
//                            }
//                            //autoLogin(id,pass)
//                            Log.d(TAG, "onCreate: 로그인 후아이디 중복 체크")
//
//                        }
//                    }
//
//                }
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY))
//                finish()
//            }
//        }
//
//    }

    //현재 사용자 정보를 담아두기 위한 전역변수
    companion object {
        var userId = ""
        var userName = ""
        var userStamp = 0
        var detailList = arrayListOf<OrderDetailDTO>()
    }
}