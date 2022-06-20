package com.ssafy.smartcafe.util

import java.text.SimpleDateFormat
import java.util.*

class Utils{

    //java의 static method와 동일
    companion object {
        fun formatter(): SimpleDateFormat {
            val formatter = SimpleDateFormat("yyyy-MM-dd hh:mm")
            formatter.timeZone = TimeZone.getTimeZone("GMT+9")

            return formatter
        }
    }

}