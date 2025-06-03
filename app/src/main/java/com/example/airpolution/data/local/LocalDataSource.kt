package com.example.airpolution.data.local

import android.content.Context

interface LocalDataSource {
    fun getDefaultCityFromSp(): String?
    fun setDefaultCity( city: String)
    fun setTemporaryCity( city: String)
    fun getTempCityFromSp(): String?
    fun removeTempCityFromSp()
}