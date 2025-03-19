package com.example.airpolution.data

interface RemoteAirValuesDataSource {
    suspend fun getAirValues(city: String):AirQualityResponse
}