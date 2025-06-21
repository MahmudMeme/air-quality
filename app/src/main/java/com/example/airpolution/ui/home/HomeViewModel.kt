package com.example.airpolution.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airpolution.data.Repository
import com.example.airpolution.data.remote.AirQualityValues
import com.example.airpolution.data.remote.AverageDataResponse
import com.example.airpolution.domain.CardAirMeasurementDisplay
import com.example.airpolution.domain.CardsViewItemUseCase
import com.example.airpolution.ui.home.datesBuilder.DateMonth
import com.example.airpolution.ui.home.datesBuilder.DateWeek
import com.example.airpolution.ui.home.datesBuilder.DateYesterday
import com.example.airpolution.ui.home.singleton.CityTemp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeStateUI(
    val text: String = "",
    val cities: List<String> = emptyList(),
    val airMeasurements: List<CardAirMeasurementDisplay> = emptyList(),
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository,
    private val cardsViewItemUseCase: CardsViewItemUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeStateUI())
    val uiState = _uiState.asStateFlow()

    fun fetchAirValues(cityName: String) {
        viewModelScope.launch {
            try {
                val url = buildUrlForCity(cityName)
                val response = repository.getAirValues(url)
                val city = response.cityName
                val values = response.values

                cardsViewBuild(values)

            } catch (e: Exception) {
                val errorMessage = "Failed to fetch air values: ${e.message}"
                _uiState.update { state ->
                    state.copy(text = errorMessage, airMeasurements = emptyList())
                }
            }
        }
    }


    private fun cardsViewBuild(values: AirQualityValues) {
        val listCards = cardsViewItemUseCase(values)

        _uiState.update { state ->
            state.copy(airMeasurements = listCards)
        }
    }

    private fun buildUrlForCity(cityName: String): String {
        return "https://${cityName}.pulse.eco/rest/overall"
    }

    fun initCities(cities: List<String>) {
        viewModelScope.launch {
            val city = CityTemp.getCity() ?: repository.getDefaultCityFromSp()
            city?.let { fetchAirValues(it) }
            val orderedCities = if (city != null && cities.contains(city)) {
                listOf(city) + cities.filter { it != city }
            } else {
                cities
            }
            val text = "now in " + city
            _uiState.update { it.copy(cities = orderedCities, text = text) }
        }
    }

    fun handleCitySelected(position: Int) {
        val selectedCityName = uiState.value.cities[position]
        fetchAirValues(selectedCityName)

        CityTemp.setCity(selectedCityName)

        val newOrderedCities =
            listOf(selectedCityName) + uiState.value.cities.filter { it != selectedCityName }

        _uiState.update { it.copy(cities = newOrderedCities) }
    }


    fun averageDataYesterday(amount: Int) {
        viewModelScope.launch {
            try {

                val city = CityTemp.getCity() ?: repository.getDefaultCityFromSp()
                city?.let {
                    val listURL = buildURLForYesterday(it, amount)

                    buildStringsResponse(city, listURL, amount)

                }
            } catch (e: Exception) {
                _uiState.update { it.copy(text = e.message.toString()) }
            }
        }
    }

    private fun buildURLForYesterday(cityName: String, amount: Int): MutableList<String> {
        var period: String = String()
        period = when (amount) {
            -1 -> "day"
            -2 -> "day"
            1 -> "week"
            else -> "month"
        }
        val valueType = listOf("pm10", "pm25", "temperature", "humidity", "noise")

        val (fromDateTime, toDateTime) = when (amount) {
            -1 -> DateYesterday.getYesterdayDateRange(amount)
            -2 -> DateYesterday.getYesterdayDateRange(amount)
            1 -> DateWeek.getPreviousWeekRangeLegacy()
            else -> DateMonth.getPreviousMonthRangeLegacy()
        }

        val listURL = mutableListOf<String>()
        for (value in valueType) {
            val url =
                "https://${cityName}.pulse.eco/rest/avgData/" +
                        "${period}?sensorId=${-1}&type=${value}&from=${fromDateTime}&to=${toDateTime}"
            listURL.add(url)
        }
        return listURL
    }
    private suspend fun buildStringsResponse(
        city: String,
        listURL: MutableList<String>,
        amount: Int,
    ) {
        val listResponse = mutableListOf<AverageDataResponse>()
        for (url in listURL) {
            val response = repository.getAverageDataForYesterday(url)
            listResponse.add(response.last())
        }
        val sb: StringBuilder = StringBuilder()
        when (amount) {
            -1 -> {
                sb.append("City $city yesterday had\n")
            }

            -2 -> {
                sb.append("City $city 2 day before today had\n")
            }

            1 -> {
                sb.append("City $city last week had\n")
            }

            else -> {
                sb.append("City $city last month had\n")
            }
        }
        val map = HashMap<String, String>()


        for (response in listResponse) {
            val info = response.type + " " + response.value + "\n"
            sb.append(info)
            map[response.type] = response.value
        }
        val values: AirQualityValues = AirQualityValues(
            no2 = null.toString(),
            pm25 = map.get("pm25") ?: null.toString(),
            o3 = null.toString(),
            pm10 = map.get("pm10") ?: null.toString(),
            temperature = map.get("temperature") ?: null.toString(),
            humidity = map.get("humidity") ?: null.toString(),
            pressure = null.toString(),
            noise_dba = map.get("noise") ?: null.toString()
        )

        _uiState.update { it.copy(text = sb.toString()) }
        cardsViewBuild(values)
    }
}