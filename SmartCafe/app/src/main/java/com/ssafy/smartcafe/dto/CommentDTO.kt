package com.ssafy.smartcafe.dto

data class CommentDTO(
    val comment: String,
    val id: Int,
    val productId: Int,
    val rating: Int,
    val userId: String
)