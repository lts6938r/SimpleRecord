package com.example.simplerecord.network

import com.example.simplerecord.api.AudioApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // 替换为你的后端FastAPI服务器的IP地址和端口
    // 如果在模拟器上运行，并且FastAPI运行在你的电脑上，通常是 10.0.2.2
    // 如果在真机上运行，替换为你的电脑在局域网中的实际IP地址
    private const val BASE_URL = "http://192.168.123.127:8000/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // 打印所有请求和响应体
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(60, TimeUnit.SECONDS) // 连接超时
        .readTimeout(90, TimeUnit.SECONDS)    // 读取超时 (为转录过程预留足够时间)
        .writeTimeout(60, TimeUnit.SECONDS)   // 写入超时
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: AudioApiService by lazy {
        retrofit.create(AudioApiService::class.java)
    }
}