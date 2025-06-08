package com.example.airpolution.data.remote

data class AirQualityResponse(
    val cityName: String,
    val values: AirQualityValues
)
data class AirQualityValues(
    val no2: String,
    val pm25: String,
    val o3: String,
    val pm10: String,
    val temperature: String,
    val humidity: String,
    val pressure: String,
    val noise_dba: String
)