package com.example.airpolution.data.remote

data class AverageDataResponse(
    val sensorId: String,
    val stamp: String,
    val type: String,
    val position: String,
    val value: String,
)