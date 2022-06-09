package com.ssafy.smartcafe.viewModel


import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssafy.smartcafe.R

class JoinViewModel:ViewModel() {

    var id = "중복확인"
    var color = MutableLiveData<Int>(0)
    var checkIdText = MutableLiveData("이미 사용중인 아이디입니다.")
    var visible = MutableLiveData(View.GONE)
    var textColor = MutableLiveData<Int>(0)

    fun setUsed(){
        checkIdText.postValue("이미 사용중인 아이디입니다.")
        visible.postValue(View.VISIBLE)
        textColor.postValue(0)
        color.postValue(0)
    }

    fun setCanUsed(){
        checkIdText.postValue("사용 가능한 아이디 입니다.")
        visible.postValue(View.VISIBLE)
        textColor.postValue(1)
        color.postValue(1)
    }





}