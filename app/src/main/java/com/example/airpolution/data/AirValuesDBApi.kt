package com.example.airpolution.data

import retrofit2.Response
import retrofit2.http.GET

interface AirValuesDBApi {
    //    @GET
//    suspend fun getAirValues(): Response<ValuesGetResponse>
    @GET("overall/")
    suspend fun getAirValues(): Response<ValuesGetResponse>
}