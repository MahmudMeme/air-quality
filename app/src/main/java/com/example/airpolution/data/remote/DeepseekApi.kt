package com.example.airpolution.data.remote

import com.example.airpolution.data.remote.deepseek.DeepSeekRequest
import com.example.airpolution.data.remote.deepseek.DeepSeekResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface DeepseekApi {
    @Headers("Content-Type: application/json")
    @POST("chat/completions")
    suspend fun getPrediction(
        @Header("Authorization") auth: String,
        @Body request: DeepSeekRequest,
    ): Response<DeepSeekResponse>
}