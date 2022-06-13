package com.ssafy.smartcafe.dto

import java.util.*

data class OrderDTO (
    var id : Int,
    var userId :String,
    var orderTable : String,
    var orderTime : Date,
    var completed : Char,
//    var datails : List<OrderDetail>,
    var stamps : Int
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


