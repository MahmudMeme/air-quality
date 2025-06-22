package com.example.airpolution.data.remote

import com.example.airpolution.data.remote.deepseek.DeepSeekRequest
import com.example.airpolution.data.remote.deepseek.DeepSeekResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url

interface AirValuesDBApi {
    @GET
    suspend fun getAirValues(@Url url: String): Response<ValuesGetResponse>

    @GET
    suspend fun getAverageDataForYesterday(@Url url: String): Response<List<AverageDataResponse>>

    @Headers("Content-Type: application/json")
    @POST("chat/completions")
    suspend fun getPrediction(
//        @Url url: String,
        @Header("Authorization") auth: String,
        @Body request: DeepSeekRequest,
    ): Response<DeepSeekResponse>
}