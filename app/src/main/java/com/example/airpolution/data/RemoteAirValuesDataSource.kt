package com.example.airpolution.data

interface RemoteAirValuesDataSource {
    suspend fun getAirValues(url: String):AirQualityResponse
}