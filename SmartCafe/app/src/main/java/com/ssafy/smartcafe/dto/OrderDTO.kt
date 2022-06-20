package com.ssafy.smartcafe.dto

import java.util.*

data class OrderDTO (
    var completed : Char,
    var datails : List<OrderDetailDTO>,
    var id : Int,
    var orderTable : String,
    var orderTime : Date,
    var stamp : List<StampDTO>,
    var userId :String,
)

data class OrderDetailDTO(
    var id: Int,
    var orderId:Int,
    var productId:Int,
    var quantity:Int
)


data class OrderDTOwithTotal(
    var img: String,
    var id : Int,
    var p_id: Int,
    var name: String,
    var order_time: String,
    var quantity: Int,
    var total: Int
)

data class RecentOrderDTO(var img:String, var quantity:Int, var user_id: String, var price:Int, var o_id:Int,
                     var name:String, var order_time:String, var completed: String, var type:String, var p_id:Int)


