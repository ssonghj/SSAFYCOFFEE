package com.ssafy.smartcafe.viewModel

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MenuDetailViewModel:ViewModel() {

    var img = MutableLiveData("")
    var name = MutableLiveData("")
    var price = MutableLiveData("")
    var rating = MutableLiveData(0.0F)
    var reviewInfo = MutableLiveData("0건의 리뷰가 있어요!")

    fun setImg(simg:String){
        img.postValue(simg)
    }

    fun setName(sname:String){
        name.postValue(sname)
    }
    fun setPrice(sprice:Int){
        price.postValue("${sprice}원")
    }
    fun setRating(srating:Float){
        rating.postValue(srating)
    }
    fun setReviewInfo(sreview:Int){
        reviewInfo.postValue("${sreview}건의 리뷰가 있어요!")
    }

}

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, img: String) {
    println("???? : ${img}")
    val resId = view.resources.getIdentifier(
            img.substring(0, img.length - 4),
            "drawable",
            "com.ssafy.smartcafe"
        )
    view.setImageResource(resId)
}