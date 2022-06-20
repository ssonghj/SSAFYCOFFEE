package com.ssafy.smartcafe.service

import com.ssafy.smartcafe.dto.CommentDTO
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface CommentService {
    @POST("comment")
    suspend fun insertComment(@Body comment: CommentDTO) : Response<Unit>

    @PUT("comment")
    suspend fun updateComment(@Body comment: CommentDTO) : Call<Unit>

    @DELETE("comment/{id}")
    suspend fun deleteComment(@Path("id") id: Int) : Response<Unit>
}