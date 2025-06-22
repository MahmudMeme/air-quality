package com.example.airpolution.ui.dashboard

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airpolution.data.Repository
import com.example.airpolution.data.remote.AverageDataResponse
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
    val text: String = "This is future fragment",
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

        val result = repository.getPrediction(promptMessage)

//        val sb: StringBuilder = StringBuilder()
//        for (response in listResponsePM10) {
//            sb.append(" ${response.stamp} ${response.type} ${response.value} \n")
//        }
//        _uiState.update { it.copy(text = sb.toString()) }
        _uiState.update { it.copy(text = result.toString()) }
    }
}
