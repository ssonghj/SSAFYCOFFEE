package com.ssafy.smartcafe.dto

data class ProductDTO(
    var id:Int,
    var name:String,
    var type:String,
    var user_id:String,
    var price:Int,
    var img:String,
    var avg:Float,
    var sells:Int,
    var rating:Int,
    var commentId: Int,
    var comment:String,
    var userName:String,
    var commentCnt:Int)

//data class ProductDTOWithCnt(
//    var id:Integer,
//    var name:String,
//    var type:String,
//    var price:Int,
//    var img:String,
//    var cnt:Integer)
