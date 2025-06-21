package com.example.airpolution.data.remote

interface RemoteAirValuesDataSource {
    suspend fun getAirValues(url: String): AirQualityResponse

    suspend fun getAverageDataForYesterday(url: String): List<AverageDataResponse>
}