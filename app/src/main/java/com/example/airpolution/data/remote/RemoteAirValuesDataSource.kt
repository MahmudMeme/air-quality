package com.example.airpolution.data.remote

import com.example.airpolution.data.remote.deepseek.DeepSeekResponse
import com.example.airpolution.data.remote.deepseek.PredictionResult

interface RemoteAirValuesDataSource {
    suspend fun getAirValues(url: String): AirQualityResponse

    suspend fun getAverageData(url: String): List<AverageDataResponse>

    suspend fun predict(prompt:String): PredictionResult
}