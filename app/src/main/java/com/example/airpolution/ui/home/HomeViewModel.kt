package com.example.airpolution.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airpolution.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _text = MutableLiveData<String>()
    val text: LiveData<String> get() = _text

    fun fetchAirValues(cityName: String) {
        viewModelScope.launch {
            try {
                val response = repository.getAirValues(cityName)
                val city = response.cityName
                val values = response.values

                val text = StringBuilder()
                text.append("Gradot ")
                text.append(city)
                text.append(" ima vrednosti ")
                text.append(values.pm10.plus(" pm10 "))
                text.append(values.temperature.plus(" temperatur "))

                _text.value = text.toString()
            } catch (e: Exception) {
                _text.value = "Failed to fetch air values: ${e.message}"
            }
        }
    }

}