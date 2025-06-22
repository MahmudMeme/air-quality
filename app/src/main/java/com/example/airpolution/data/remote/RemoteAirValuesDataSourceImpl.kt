package com.example.airpolution.data.remote

import com.example.airpolution.data.remote.deepseek.DeepSeekApi
import com.example.airpolution.data.remote.deepseek.DeepSeekRequest
import com.example.airpolution.data.remote.deepseek.Message
import com.example.airpolution.data.remote.deepseek.PredictionResult
import javax.inject.Inject

class RemoteAirValuesDataSourceImpl @Inject constructor(
    private val airValuesDBApi: AirValuesDBApi,
) : RemoteAirValuesDataSource {
    override suspend fun getAirValues(url: String): AirQualityResponse {

        val airValuesResponse = airValuesDBApi.getAirValues(url)
        val bodyResponse = airValuesResponse.body()
        if (airValuesResponse.isSuccessful && bodyResponse != null) {
            return AirQualityResponse(bodyResponse.cityName, bodyResponse.values)
        }
        throw Exception("padna vo retrofit air values data source")
    }

    override suspend fun getAverageData(url: String): List<AverageDataResponse> {
        val response = airValuesDBApi.getAverageDataForYesterday(url)
        val bodyResponse = response.body()
        if (response.isSuccessful && bodyResponse != null) {
            return bodyResponse
        }
        throw Exception("padna vo retrofit air values funkcija za podatoci od vcera")
    }

    override suspend fun predict(prompt: String): PredictionResult {
        val messages = listOf(
            Message(
                role = "system",
                content = "You are an AI assistant specialized in environmental data analysis and prediction."
            ),
            Message(
                role = "user",
                content = prompt
            )
        )
        val request = DeepSeekRequest(messages = messages)

        return try {
            val response = airValuesDBApi.getPrediction(
                auth = "Bearer ${DeepSeekApi.API_KEY}",
                request = request
            )

            if (response.isSuccessful) {
                response.body()?.choices?.firstOrNull()?.message?.content?.let {
                    PredictionResult.Success(it)
                } ?: PredictionResult.Error("Empty response from API")
            } else {
                val error = response.errorBody()?.string() ?: "Unknown error"
                PredictionResult.Error("API error: $error")
            }
        } catch (e: Exception) {
            PredictionResult.Error("Network error: ${e.message}")
        }
    }
}

// Custom exception class
