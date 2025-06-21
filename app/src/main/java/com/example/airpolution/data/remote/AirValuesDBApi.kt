package com.example.airpolution.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface AirValuesDBApi {
    @GET
    suspend fun getAirValues(@Url url: String): Response<ValuesGetResponse>

    @GET
    suspend fun getAverageDataForYesterday(@Url url: String): Response<List<AverageDataResponse>>
}