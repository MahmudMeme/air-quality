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
class HomeViewModel @Inject constructor(private val reposirotry: Repository) : ViewModel() {
    fun setTextWithValues(): String {
        viewModelScope.launch {
            val response = reposirotry.getAirValues()
            val city = response.cityName
            val values = response.values

            var text = StringBuilder()
            text.append("gtradot ")
            text.append(city)
            text.append(" ima vrednosti ")
            text.append(values.pm10.plus(" pm10 "))
            text.append(values.temperature.plus(" temperatur "))

        }
        return text.toString()
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}