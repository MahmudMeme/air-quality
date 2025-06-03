package com.example.airpolution.data.local

import android.content.Context
import android.content.Context.MODE_PRIVATE
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(private val context: Context)
    :LocalDataSource {
    override fun getDefaultCityFromSp(): String? {
        val sp = context.getSharedPreferences("airCity", MODE_PRIVATE)
        val city = sp.getString("defaultCity", null)
//        city?.let { fetchAirValues(it) }
        return city
    }

    override fun setDefaultCity(city: String) {
        val sp = context.getSharedPreferences("airCity", MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString("defaultCity", city)
        editor.apply()
    }

    override fun setTemporaryCity(city: String) {
        val sp = context.getSharedPreferences("airCity", MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString("tempCity", city)
        editor.apply()
    }

    override fun getTempCityFromSp(): String? {
        val sp = context.getSharedPreferences("airCity", MODE_PRIVATE)
        val city = sp.getString("tempCity", null)
//        city?.let { fetchAirValues(it) }
        return city
    }

    override fun removeTempCityFromSp() {
        val sp = context.getSharedPreferences("airCity", MODE_PRIVATE)
        val editor = sp.edit()
        editor.remove("tempCity")
        editor.apply()
    }

}