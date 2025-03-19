package com.example.airpolution.data

import javax.inject.Inject

class Repository @Inject constructor(private val remoteAirValuesDataSource: RemoteAirValuesDataSource) {
    suspend fun getAirValues(city: String): AirQualityResponse {
        return remoteAirValuesDataSource.getAirValues(city)
    }
}