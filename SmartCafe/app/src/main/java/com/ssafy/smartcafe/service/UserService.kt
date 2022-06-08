package com.ssafy.smartcafe.service

import com.ssafy.smartcafe.dto.UserDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {
    //회원 정보 추가하기
    @POST("user")
    fun userInsert(@Body dto: UserDTO) : Call<Unit>

    //회원 정보 조회 (id)
    @POST("user/info")
    fun getUser(@Query("id") id: String): Call<Map<String,Object>>

    //사용 여부 반환
    @GET("user/isUsed")
    fun checkUser(@Query("id") id: String): Call<Boolean>

    //로그인 처리 후 성공여부 반환
    @POST("user/login")
    fun userLogin(@Body dto: UserDTO) : Call<UserDTO>

}