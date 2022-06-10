package com.ssafy.smartcafe.dto

data class ProductDTO(
    var id:Integer,
    var name:String,
    var type:String,
    var price:Int,
    var img:String)

//data class ProductDTOWithCnt(
//    var id:Integer,
//    var name:String,
//    var type:String,
//    var price:Int,
//    var img:String,
//    var cnt:Integer)