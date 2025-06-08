package com.example.airpolution.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airpolution.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeStateUI(
    val text: String = "",
    val cities: List<String> = emptyList(),
)

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeStateUI())
    val uiState = _uiState.asStateFlow()

    fun fetchAirValues(cityName: String) {
        viewModelScope.launch {
            try {
                val url = buildUrlForCity(cityName)
                val response = repository.getAirValues(url)
                val city = response.cityName
                val values = response.values

                val text = StringBuilder()
                text.append("Gradot ")
                text.append(city)
                text.append(" ima vrednosti ")
                text.append(values.pm10.plus(" pm10 "))
                text.append(values.temperature.plus(" temperatur "))

                _uiState.update { state ->
                    state.copy(text = text.toString())
                }

            } catch (e: Exception) {
                val errorMessage = "Failed to fetch air values: ${e.message}"
                _uiState.update { state ->
                    state.copy(text = errorMessage)
                }
            }
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