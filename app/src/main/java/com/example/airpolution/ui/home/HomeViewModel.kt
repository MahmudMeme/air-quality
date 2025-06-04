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
    val city: String? = "",
)

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeStateUI())
    val uiState = _uiState.asStateFlow()
    var cityDef:String?=null

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

    fun getDefaultCityFromSp(): String? {
        viewModelScope.launch {
            val city = repository.getDefaultCityFromSp()
            city?.let { fetchAirValues(it) }
            _uiState.update { state ->
                state.copy(city=city)
            }
            cityDef = city
        }
        return cityDef
    }


    fun setTemporaryCity(city: String) {
        CityTemp.setCity(city)
    }

    fun getTempCityFromSp(): String? {
        return CityTemp.getCity()
    }

    fun removeTempCityFromSp() {
        viewModelScope.launch {
            repository.getDefaultCityFromSp()?.let { CityTemp.setCity(it) }
        }
    }

    private fun buildUrlForCity(cityName: String): String {
        return "https://${cityName}.pulse.eco/rest/overall"
    }

}