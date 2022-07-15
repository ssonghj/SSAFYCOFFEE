package com.ssafy.smartcafe.util

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import java.text.SimpleDateFormat
import java.util.*

class Utils {

    //java의 static method와 동일
    companion object {
        fun formatter(): SimpleDateFormat {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
//            val formatter = SimpleDateFormat("yyyy-MM-dd hh:mm")
            formatter.timeZone = TimeZone.getTimeZone("GMT+9")

            return formatter
        }
    }

}