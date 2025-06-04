package com.example.airpolution.ui.home

object CityTemp {
    private var cityTemp: String? = null

    fun getCity(): String? {
        return cityTemp
    }

    fun setCity(city: String) {
        cityTemp = city
    }
}