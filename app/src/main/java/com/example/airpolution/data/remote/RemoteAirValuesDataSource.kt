package com.example.airpolution.data.remote

import com.example.airpolution.data.remote.deepseek.PredictionResult

interface RemoteAirValuesDataSource {
    suspend fun getAirValues(city: String): AirQualityResponse
    // TODO: Refactor, Move the logic from VM to RemoteDataSource
    suspend fun getAverageData(url: String): List<AverageDataResponse>
    suspend fun predict(city: String): PredictionResult
}