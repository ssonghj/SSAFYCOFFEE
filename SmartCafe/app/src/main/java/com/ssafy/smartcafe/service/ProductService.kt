package com.ssafy.smartcafe.service

import com.ssafy.smartcafe.dto.ProductDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductService {

    //메뉴 정보 모두 반환
    @GET("product")
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

    //추천 메뉴 반환
    @GET("product/recommendedProduct")
    fun selectRecommendedProduct(): Call<List<ProductDTO>>
}