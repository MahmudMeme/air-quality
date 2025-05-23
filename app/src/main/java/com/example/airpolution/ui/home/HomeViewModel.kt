package com.example.airpolution.ui.home

import android.content.Context
import android.content.Context.MODE_PRIVATE
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
    val text: String = ""
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
    fun getDefaultCityFromSp(context: Context): String? {
        val sp = context.getSharedPreferences("airCity", MODE_PRIVATE)
        val city = sp.getString("defaultCity", null)
        city?.let { fetchAirValues(it) }
        return city
    }
    fun setTemporaryCity(context: Context, city: String) {
        val sp = context.getSharedPreferences("airCity", MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString("tempCity", city)
        editor.apply()
    }
    fun getTempCityFromSp(context: Context): String? {
        val sp = context.getSharedPreferences("airCity", MODE_PRIVATE)
        val city = sp.getString("tempCity", null)
        city?.let { fetchAirValues(it) }
        return city
    }

    private fun buildUrlForCity(cityName: String): String {
        return "https://${cityName}.pulse.eco/rest/overall"
    }

}