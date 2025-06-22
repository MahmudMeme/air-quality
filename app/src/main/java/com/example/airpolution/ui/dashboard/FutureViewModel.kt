package com.example.airpolution.ui.dashboard

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airpolution.data.Repository
import com.example.airpolution.data.remote.AverageDataResponse
import com.example.airpolution.data.remote.deepseek.PredictionResult
import com.example.airpolution.domain.ParseJSON
import com.example.airpolution.domain.TableForPrediction
import com.example.airpolution.ui.dashboard.datesBuilder.DateForPrediction
import com.example.airpolution.ui.home.singleton.CityTemp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FutureStateUI(
    val text: String = "Press the button to predict",
    val moreInfo: String = "",
)

@HiltViewModel
class FutureViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _uiState = MutableStateFlow(FutureStateUI())
    val uiState = _uiState.asStateFlow()


    fun loadData() {
        viewModelScope.launch {
            try {
                val city = CityTemp.getCity() ?: repository.getDefaultCityFromSp()
                city?.let {
                    _uiState.update { it.copy(text = "Loading") }
                    val listUrl = buildURLForPrediction(city)

                    buildPrompt(listUrl)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(text = e.message.toString()) }
            }
        }
    }

    private fun buildURLForPrediction(cityName: String): MutableList<String> {
        val period = "day"
        val valueType = listOf("pm10", "pm25", "temperature", "humidity", "noise")
        val (fromDateTime, toDateTime) = DateForPrediction.getLast30DaysRange()

        val listURL = mutableListOf<String>()
        for (value in valueType) {
            val url =
                "https://${cityName}.pulse.eco/rest/avgData/" +
                        "${period}?sensorId=${-1}&type=${value}&from=${fromDateTime}&to=${toDateTime}"
            listURL.add(url)
        }
        return listURL
    }

    @SuppressLint("NewApi")
    private suspend fun buildPrompt(listURL: MutableList<String>) {
        val listResponsePM10 = mutableListOf<AverageDataResponse>()
        val listResponsePM25 = mutableListOf<AverageDataResponse>()
        val listResponseTemperature = mutableListOf<AverageDataResponse>()
        val listResponseHumidity = mutableListOf<AverageDataResponse>()
        val listResponseNoise = mutableListOf<AverageDataResponse>()
        listResponsePM10.addAll(repository.getAverageData(listURL.get(0)))
        listResponsePM25.addAll(repository.getAverageData(listURL.get(1)))
        listResponseTemperature.addAll(repository.getAverageData(listURL.get(2)))
        listResponseHumidity.addAll(repository.getAverageData(listURL.get(3)))
        listResponseNoise.addAll(repository.getAverageData(listURL.get(4)))

        val listPm10 = listResponsePM10.stream().map { it.value.toIntOrNull() }.toList()
        val listPm25 = listResponsePM25.stream().map { it.value.toIntOrNull() }.toList()
        val listTemperature =
            listResponseTemperature.stream().map { it.value.toIntOrNull() }.toList()
        val listHumidity = listResponseHumidity.stream().map { it.value.toIntOrNull() }.toList()
        val listNoise = listResponseNoise.stream().map { it.value.toIntOrNull() }.toList()

        val promptMessage = TableForPrediction.buildPredictionPrompt(
            listPm10,
            listPm25,
            listTemperature,
            listHumidity,
            listNoise
        )

        when (val prediction = repository.getPrediction(promptMessage)) {
            is PredictionResult.Success -> {
                val data = prediction.data
                val result = ParseJSON.parsePredictionResponse(data)
                val moreInfo = "${result.get(1)} \n ${result.get(2)}"
                _uiState.update { it.copy(text = result.get(0), moreInfo = moreInfo) }
            }

            is PredictionResult.Error -> {
                _uiState.update { it.copy(text = prediction.message) }
            }
        }

//        val data = ValidMeesage
//        val result = ParseJSON.parsePredictionResponse(data)
//        val moreInfo = "${result.get(1)} \n ${result.get(2)}"
//        _uiState.update { it.copy(text = result.get(0), moreInfo = moreInfo) }
    }
}

val ValidMeesage = "```json\n" +
        "{\n" +
        "    \"prediction\": {\n" +
        "        \"date\": \"2025-06-23\",\n" +
        "        \"pm10\": {\n" +
        "            \"value\": 9,\n" +
        "            \"confidence\": 75,\n" +
        "            \"trend\": \"stable\",\n" +
        "            \"expected_range\": [7, 12]\n" +
        "        },\n" +
        "        \"pm25\": {\n" +
        "            \"value\": 5,\n" +
        "            \"confidence\": 80,\n" +
        "            \"trend\": \"stable\",\n" +
        "            \"expected_range\": [4, 7]\n" +
        "        },\n" +
        "        \"temperature\": {\n" +
        "            \"value\": 26,\n" +
        "            \"confidence\": 85,\n" +
        "            \"trend\": \"stable\",\n" +
        "            \"expected_range\": [25, 27]\n" +
        "        },\n" +
        "        \"humidity\": {\n" +
        "            \"value\": 35,\n" +
        "            \"confidence\": 80,\n" +
        "            \"trend\": \"stable\",\n" +
        "            \"expected_range\": [32, 40]\n" +
        "        },\n" +
        "        \"noise\": {\n" +
        "            \"value\": 75,\n" +
        "            \"confidence\": 70,\n" +
        "            \"trend\": \"stable\",\n" +
        "            \"expected_range\": [72, 78]\n" +
        "        }\n" +
        "    },\n" +
        "    \"key_factors\": [\n" +
        "        \"Recent drop in PM levels correlated with lower humidity\",\n" +
        "        \"Stable temperature and humidity suggest consistent air quality\",\n" +
        "        \"No significant weekday/weekend patterns observed in the data\",\n" +
        "        \"No apparent lag effects from previous days\"\n" +
        "    ],\n" +
        "    \"health_implications\": \"Air quality is expected to be good with PM2.5 and PM10 levels well below concerning thresholds. No significant health risks anticipated.\",\n" +
        "    \"model_notes\": \"Assumed linear relationships between PM levels and meteorological factors. Missing data for 2025-06-22 was compensated by using the average of the surrounding days. Limited to 30 days of data, which may not capture longer-term trends or seasonal variations.\"\n" +
        "}\n" +
        "```"