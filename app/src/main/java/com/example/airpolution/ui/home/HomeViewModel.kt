package com.example.airpolution.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airpolution.data.Repository
import com.example.airpolution.data.remote.AirQualityValues
import com.example.airpolution.domain.CardAirMeasurementDisplay
import com.example.airpolution.domain.CardsViewItemUseCase
import com.example.airpolution.ui.home.singleton.CityTemp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeStateUI(
    val errorText: String = "",
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
                    state.copy(errorText = errorMessage)
                    state.copy(airMeasurements = emptyList())
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
            _uiState.update { it.copy(cities = orderedCities) }
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

}