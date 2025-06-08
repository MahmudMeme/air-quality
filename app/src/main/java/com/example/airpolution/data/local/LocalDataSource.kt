package com.example.airpolution.data.local

interface LocalDataSource {
    suspend fun getDefaultCityFromSp(): String?
    suspend fun setDefaultCity(city: String)
}