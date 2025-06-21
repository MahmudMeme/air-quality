package com.example.airpolution.ui.home.singleton

object CityTemp {
    private var cityTemp: String? = null

    fun getCity(): String? {
        return cityTemp
    }

    fun setCity(city: String) {
        cityTemp = city
    }
}