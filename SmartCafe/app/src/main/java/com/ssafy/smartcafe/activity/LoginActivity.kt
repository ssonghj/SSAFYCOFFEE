package com.ssafy.smartcafe.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.shashank.sony.fancytoastlib.FancyToast
import com.ssafy.smartcafe.MobileCafeApplication
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.databinding.ActivityLoginBinding
import com.ssafy.smartcafe.dto.UserDTO
import com.ssafy.smartcafe.service.UserService
import kotlinx.coroutines.*


private const val TAG = "LoginActivity"
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var user:UserDTO

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


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
    }

    private suspend fun getIDPass(inputUser:UserDTO, id:String, pass:String) {

        withContext(Dispatchers.IO){
            val service = MobileCafeApplication.retrofit.create(UserService::class.java)
            val response = service.userLogin(inputUser).execute()

            if(response.code() == 200){
                var res = response.body() ?: null

                user = res!!

                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed(java.lang.Runnable {
                    FancyToast.makeText(applicationContext,
                        "아이디 혹은 비밀번호가 일치하지 않습니다.",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.CONFUSING,
                        false).show()
                }, 0)

                if(user.id == id && user.pass == pass){
                    val intent = Intent(applicationContext, MainActivity::class.java);
                    startActivity(intent)
                }

                Log.d(TAG, "getIDPass: ${res}")
            }else{
                Log.d(TAG, "getIDPass: error code ${response.code()}")
            }
        }

    }
}