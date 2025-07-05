package com.example.airpolution.data.remote

import android.annotation.SuppressLint
import com.example.airpolution.data.remote.deepseek.DeepSeekApi
import com.example.airpolution.data.remote.deepseek.DeepSeekRequest
import com.example.airpolution.data.remote.deepseek.Message
import com.example.airpolution.data.remote.deepseek.PredictionResult
import com.example.airpolution.domain.ParseJSON
import com.example.airpolution.domain.TableForPrediction
import com.example.airpolution.ui.dashboard.datesBuilder.DateForPrediction
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class RemoteAirValuesDataSourceImpl @Inject constructor(
    private val airValuesDBApi: AirValuesDBApi,
    private val deepseekApi: DeepseekApi,
) : RemoteAirValuesDataSource {
    override suspend fun getAirValues(city: String): AirQualityResponse {
        val cityUrl = "https://$city.pulse.eco/rest/overall"
        val airValuesResponse = airValuesDBApi.getAirValues(cityUrl)
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

    override suspend fun predict(city: String): PredictionResult {
        val urls = buildURLForPrediction(city)
        val promptMessage = buildPrompt(urls)

        val messages = listOf(
            Message(
                role = "system",
                content = "You are an AI assistant specialized in environmental data analysis and prediction."
            ),
            Message(
                role = "user", content = promptMessage
            )
        )
        val request = DeepSeekRequest(messages = messages)

        val data = try {
            val response = withTimeout(35_000) {
                deepseekApi.getPrediction(
                    auth = "Bearer ${DeepSeekApi.API_KEY}",
                    request = request
                )
            }

            if (response.isSuccessful) {
                response.body()?.choices?.firstOrNull()?.message?.content?.let {
                    it
                } ?: return PredictionResult.Error("Empty response from API")
            } else {
                val error = response.errorBody()?.string() ?: "Unknown error"
                return PredictionResult.Error("API error: $error")
            }
        } catch (e: Exception) {
            return PredictionResult.Error("Network error: ${e.message}")
        }

        val result = ParseJSON.parsePredictionResponse(data)
        val moreInfo = "${result[1]} \n ${result[2]}"

        return PredictionResult.Success(result[0], moreInfo)
    }

    private fun buildURLForPrediction(cityName: String): MutableList<String> {
        val period = "day"
        val valueType = listOf("pm10", "pm25", "temperature", "humidity", "noise")
        val (fromDateTime, toDateTime) = DateForPrediction.getLast30DaysRange()

        val listURL = mutableListOf<String>()
        for (value in valueType) {
            val url =
                "https://${cityName}.pulse.eco/rest/avgData/" + "${period}?sensorId=${-1}&type=${value}&from=${fromDateTime}&to=${toDateTime}"
            listURL.add(url)
        }
        return listURL
    }

    @SuppressLint("NewApi")
    private suspend fun buildPrompt(listURL: MutableList<String>): String {
        val listPm10 = getAverageData(listURL[0]).map { it.value.toIntOrNull() }
        val listPm25 = getAverageData(listURL[1]).map { it.value.toIntOrNull() }
        val listTemperature = getAverageData(listURL[2]).map { it.value.toIntOrNull() }
        val listHumidity = getAverageData(listURL[3]).map { it.value.toIntOrNull() }
        val listNoise = getAverageData(listURL[4]).map { it.value.toIntOrNull() }

        val promptMessage = TableForPrediction.buildPredictionPrompt(
            listPm10, listPm25, listTemperature, listHumidity, listNoise
        )

        return promptMessage
    }
}
