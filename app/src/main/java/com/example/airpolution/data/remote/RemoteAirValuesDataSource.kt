package com.example.airpolution.data.remote

interface RemoteAirValuesDataSource {
    suspend fun getAirValues(url: String): AirQualityResponse

    suspend fun getAverageData(url: String): List<AverageDataResponse>
}