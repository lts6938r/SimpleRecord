package com.example.simplerecord.data

data class Post(
    val id: Int,
    val title: String,
    val body: String, // 假设 API 返回 body 字段
    val userId: Int // 假设 API 返回 userId 字段
)
