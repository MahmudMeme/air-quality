package com.example.airpolution.data.local

import android.content.Context
import android.content.Context.MODE_PRIVATE
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(private val context: Context) : LocalDataSource {
    override suspend fun getDefaultCityFromSp(): String? {
        val sp = context.getSharedPreferences("airCity", MODE_PRIVATE)
        val city = sp.getString("defaultCity", null)
        return city
    }

    override suspend fun setDefaultCity(city: String) {
        val sp = context.getSharedPreferences("airCity", MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString("defaultCity", city)
        editor.apply()
    }
}