package com.ssafy.smartcafe.dto

data class StampDTO(
    var id:Int,
    var orderId:Int,
    var quantity:Int,
    var userId:String
)

data class StampDTONoId(
    var orderId:Int,
    var quantity:Int,
    var userId:String
)