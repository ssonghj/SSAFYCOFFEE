package com.ssafy.smartcafe.service

import com.ssafy.smartcafe.dto.CommentDTO
import com.ssafy.smartcafe.dto.ProductDTO
import com.ssafy.smartcafe.dto.UserLikeDTO
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ProductService {

    //메뉴 정보 모두 반환
    @GET("product/productAll")
    fun selectAllProduct(): Call<List<ProductDTO>>

    //메뉴 정보 하나 반환
    @GET("product/{productId}")
    fun selectProduct(@Path("productId") productId:Int): Call<List<ProductDTO>>

    //메뉴 인기순 반환
    @GET("product/sellCount")
    fun selectProductWithSell(): Call<List<ProductDTO>>

    //낮은 가격순 반환
    @GET("product/rowPrice")
    fun selectProductWithRowPrice(): Call<List<ProductDTO>>

    //높은 가격순 반환
    @GET("product/highPrice")
    fun selectProductWithHighPrice(): Call<List<ProductDTO>>

    //리뷰 많은 순 반환
    @GET("product/highCommentCnt")
    fun selectProductWithHighCommentCnt(): Call<List<ProductDTO>>

    //평점순 반환
    @GET("product/highRating")
    fun selectProductWithHighRating(): Call<List<ProductDTO>>

    //모든 음료 반환
    @GET("product/coffee")
    fun selectAllCoffee(): Call<List<ProductDTO>>

    //모든 디저트 반환
    @GET("product/desert")
    fun selectAllDesert(): Call<List<ProductDTO>>

    //신상 메뉴 반환
    @GET("product/newProduct")
    fun selectNewProduct(): Call<List<ProductDTO>>

    //유저의 찜한 메뉴만 반환
    @GET("product/userLike/{userId}")
    fun selectUserLikeMenu(@Path("userId") userId:String): Call<List<ProductDTO>>

    @POST("product/insertLikeMenu")
    suspend fun insertLikeMenu(@Body userLike: UserLikeDTO) : Response<Unit>

    @POST("product/removeLikeMenu")
    suspend fun removeLikeMenu(@Body userLike: UserLikeDTO) : Response<Unit>

    //추천 메뉴 반환
    @GET("product/recommendedProduct")
    fun selectRecommendedProduct(): Call<List<ProductDTO>>
    
    //디저트 추천 메뉴 반환
    @GET("product/recommendedDesert")
    fun selectRecommendedDesert(): Call<List<ProductDTO>>
}