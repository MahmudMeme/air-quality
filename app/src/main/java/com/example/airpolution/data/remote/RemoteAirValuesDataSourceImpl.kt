package com.example.airpolution.data.remote

import com.example.airpolution.data.remote.deepseek.DeepSeekApi
import com.example.airpolution.data.remote.deepseek.DeepSeekRequest
import com.example.airpolution.data.remote.deepseek.Message
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

    override suspend fun predict(prompt: String): String? {
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

        try {
            val response =
                airValuesDBApi.getPrediction("Bearer ${DeepSeekApi.API_KEY}", request)

            if (response.isSuccessful) {
                val predictionJson = response.body()?.choices?.firstOrNull()?.message?.content
                return predictionJson
            } else {
                throw Exception("API request failed: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            throw Exception("Prediction failed: ${e.message}")
        }
    }
}