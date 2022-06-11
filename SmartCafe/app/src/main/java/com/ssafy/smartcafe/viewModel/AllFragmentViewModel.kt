package com.ssafy.smartcafe.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AllFragmentViewModel: ViewModel() {
    private var img = MutableLiveData("ssafycoffee.png")
    var name = MutableLiveData("이름")
    var price = MutableLiveData("1000원")

    var isDrink = MutableLiveData(0)

    fun getImg(): MutableLiveData<String> {
        return this.img
    }

    fun setImg(simg:String){
        this.img.postValue(simg)
    }
    fun setName(sname:String){
        this.name.postValue(sname)
    }
    fun setPrice(sprice: String){
        this.price.postValue(sprice)
    }

    fun changeDrink(){
        isDrink.postValue(0)

    }
    fun changeDesert(){
        isDrink.postValue(1)
    }
}