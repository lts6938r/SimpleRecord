package com.example.simplerecord.api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

// 定义后端返回的转录结果结构
data class TranscriptionResponse(
    val transcript: String
)

interface AudioApiService {
    @Multipart
    @POST("transcribe") // 后端FastAPI的转录端点
    suspend fun transcribeAudio(
        @Part audio_file: MultipartBody.Part // 确保参数名与FastAPI中的`audio_file`匹配
    ): Response<TranscriptionResponse> // 使用 Response<T> 可以获取更多HTTP信息
}
