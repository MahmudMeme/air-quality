package com.example.airpolution.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface AirValuesDBApi {
    //    @GET("rest/overall/")
//    suspend fun getAirValues(): Response<ValuesGetResponse>
    @GET
    suspend fun getAirValues(@Url url: String): Response<ValuesGetResponse>
}