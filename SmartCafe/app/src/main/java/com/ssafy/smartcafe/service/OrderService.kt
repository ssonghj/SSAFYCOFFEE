package com.ssafy.smartcafe.service

import com.ssafy.smartcafe.dto.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface OrderService {
    @POST("order")
    suspend fun insertOrder(@Body orderDTO: OrderDTO): Response<Unit>
//
//    //주문상세내역 목록형태로 반환
//    @GET("order/{orderId}")
//    fun selectOrderDetail(@Path("orderId") orderId:Int): Call<List<OrderDetailDTO2>>
//
//    //주문상세내역 목록형태로 반환
//    @GET("order/{orderId}")
//    fun selectOrderDTO3(@Path("orderId") orderId:Int): Call<List<OrderDTO3>>

    //최근 1개월주문정보 반환
    @GET("order/byUser")
    fun selectRecentOrder(@Query("id") id:String): Call<List<RecentOrderDTO>>

    //이번주 가장 많이 팔린 상품 반환
    @GET("order/thisWeekTop3")
    fun selectThisWeekOrder(): Call<List<OrderDTOwithTotal>>

    //주문 중인 내용 반환
    @GET("order/getCurOrder")
    fun selectCurOrder(@Query("userId") id:String): Call<List<RecentOrderDTO>>

    //주문했던 내용 반환
    @GET("order/getPastOrder")
    fun selectPastOrder(@Query("userId") id:String): Call<List<RecentOrderDTO>>

    //리뷰 쓸 것들 반환
    @GET("order/getWriteComment")
    fun selectWriteComment(@Query("userId")userId: String): Call<List<RecentOrderDTOwithComment>>

    @GET("order/updateIsWriteComment")
    suspend fun updateWriteComment(@Query("dId") dId: Int) : Response<Unit>
}