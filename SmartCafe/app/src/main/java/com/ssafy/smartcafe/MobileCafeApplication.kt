package com.ssafy.smartcafe

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MobileCafeApplication: Application() {
    // Application 인스턴스는 앱이 최초 실행될 때 생성되거나
    // 앱 프로세스 소멸로 인해 인스턴스가 소멸된 후 앱이 다시 실행될 때 재생성된다.
    // Manifest에 등록해야 한다.

    //        private val BOARD_URL = "http://192.168.123.106:9999/rest/"
    private val BOARD_URL = "http://192.168.123.107:9999/rest/"
//    private val BOARD_URL = "http://192.168.35.53:9999/rest/"
//    private val BOARD_URL = "http://172.20.10.10:9999/rest/"



    companion object {
        // 전역변수 문법을 통해 Retrofit 인스턴스를 앱 실행 시 1번만 생성하여 사용 (싱글톤 객체)
        lateinit var retrofit: Retrofit
        lateinit var prefs: PreferenceUtil
    }

    override fun onCreate() {
        super.onCreate()

        // 앱이 처음 생성되는 순간, retrofit 인스턴스를 생성
        retrofit = Retrofit.Builder()
            .baseUrl(BOARD_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        prefs = PreferenceUtil(applicationContext)
    }
}

class PreferenceUtil(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
    fun getString(key: String, defValue: String): String {
        return prefs.getString(key, defValue).toString()
    }
    fun setString(key: String, str: String) {
        prefs.edit().putString(key, str).apply()
    }
}

