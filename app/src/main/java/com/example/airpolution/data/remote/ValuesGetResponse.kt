package com.example.airpolution.data.remote

data class ValuesGetResponse(val page: Int, val cityName:String, val values: AirQualityValues)
