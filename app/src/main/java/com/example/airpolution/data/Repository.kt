package com.example.airpolution.data

import com.example.airpolution.data.local.LocalDataSource
import com.example.airpolution.data.remote.AirQualityResponse
import com.example.airpolution.data.remote.AverageDataResponse
import com.example.airpolution.data.remote.RemoteAirValuesDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val remoteAirValuesDataSource: RemoteAirValuesDataSource,
    private val localDataSource: LocalDataSource,
) {
    suspend fun getAirValues(url: String): AirQualityResponse {
        return remoteAirValuesDataSource.getAirValues(url)
    }

    suspend fun getDefaultCityFromSp(): String? {
        return localDataSource.getDefaultCityFromSp()
    }

    suspend fun setDefaultCity(city: String) {
        localDataSource.setDefaultCity(city)
    }
    suspend fun getAverageDataForYesterday(url: String): List<AverageDataResponse> {
        return remoteAirValuesDataSource.getAverageDataForYesterday(url)
    }
}