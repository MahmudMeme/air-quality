package com.example.airpolution.domain

import android.graphics.Color
import com.example.airpolution.R
import com.example.airpolution.data.remote.AirQualityValues
import javax.inject.Inject

class CardsViewItemUseCase @Inject constructor() {

    operator fun invoke(values: AirQualityValues): List<CardAirMeasurementDisplay> {
        val list: List<CardAirMeasurementDisplay> = listOf(
            CardAirMeasurementDisplay(
                "pm10",
                values.pm10.toInt(),
                displayText = "pm10: " + values.pm10,
                color = getPm10Color(values.pm10),
                imagePath = R.drawable.pm10_50white
            ), CardAirMeasurementDisplay(
                "pm25",
                values.pm25.toInt(),
                displayText = "pm25: " + values.pm25,
                color = getPm25Color(values.pm25),
                imagePath = R.drawable.pm10_50white
            ), CardAirMeasurementDisplay(
                "temperature",
                values.temperature.toInt(),
                displayText = "Temperature: " + values.temperature,
                color = getTemperatureColor(values.temperature),
                imagePath = R.drawable.temperature_50
            ), CardAirMeasurementDisplay(
                "o3",
//                values.o3.toInt(),
                0,
                displayText = "o3: " + values.o3,
                color = getO3Color(values.o3),
                imagePath = R.drawable.o2_50
            ), CardAirMeasurementDisplay(
                "no2",
                values.no2.toInt(),
                displayText = "no2: " + values.no2,
                color = getNo2Color(values.no2),
                imagePath = R.drawable.no2
            ), CardAirMeasurementDisplay(
                "noise",
                values.noise_dba.toInt(),
                displayText = "Noise: " + values.noise_dba,
                color = getNoiseColor(values.noise_dba),
                imagePath = R.drawable.noise_50
            ), CardAirMeasurementDisplay(
                "pressure",
                values.pressure.toInt(),
                displayText = "Pressure: " + values.pressure,
                color = getPressureColor(values.pressure),
                imagePath = R.drawable.pressure_50
            ), CardAirMeasurementDisplay(
                "humidity",
                values.humidity.toInt(),
                displayText = "Humidity: " + values.humidity,
                color = getHumidityColor(values.humidity),
                imagePath = R.drawable.humidity_50
            )
        )
        return list
    }

    private fun getTemperatureColor(tempStr: String): Int {
        val temp = tempStr.toFloatOrNull() ?: return Color.GRAY
        return when {
            temp < 0 -> Color.parseColor("#00BFFF") // DeepSkyBlue
            temp < 15 -> Color.parseColor("#ADD8E6") // LightBlue
            temp < 25 -> Color.parseColor("#90EE90") // LightGreen
            temp < 30 -> Color.YELLOW
            else -> Color.RED
        }
    }

    private fun getPm10Color(pm10Str: String): Int {
        val pm10 = pm10Str.toFloatOrNull() ?: return Color.GRAY
        return when {
            pm10 <= 20 -> Color.parseColor("#90EE90") // Good
            pm10 <= 50 -> Color.YELLOW // Moderate
            pm10 <= 90 -> Color.parseColor("#FFA500") // Unhealthy for sensitive
            else -> Color.RED // Unhealthy
        }
    }

    private fun getPm25Color(pm25Str: String): Int {
        val pm25 = pm25Str.toFloatOrNull() ?: return Color.GRAY
        return when {
            pm25 <= 10 -> Color.parseColor("#90EE90") // Good
            pm25 <= 25 -> Color.YELLOW
            pm25 <= 50 -> Color.parseColor("#FFA500")
            else -> Color.RED
        }
    }

    private fun getO3Color(o3Str: String): Int {
        val o3 = o3Str.toFloatOrNull() ?: return Color.GRAY
        return when {
            o3 <= 60 -> Color.parseColor("#90EE90")
            o3 <= 120 -> Color.YELLOW
            o3 <= 180 -> Color.parseColor("#FFA500")
            else -> Color.RED
        }
    }

    private fun getNo2Color(no2Str: String): Int {
        val no2 = no2Str.toFloatOrNull() ?: return Color.GRAY
        return when {
            no2 <= 50 -> Color.parseColor("#90EE90")
            no2 <= 100 -> Color.YELLOW
            no2 <= 200 -> Color.parseColor("#FFA500")
            else -> Color.RED
        }
    }

    private fun getNoiseColor(noiseStr: String): Int {
        val noise = noiseStr.toFloatOrNull() ?: return Color.GRAY
        return when {
            noise < 30 -> Color.parseColor("#90EE90") // Quiet
            noise < 60 -> Color.YELLOW // Moderate
            noise < 80 -> Color.parseColor("#FFA500") // Loud
            else -> Color.RED // Very loud
        }
    }

    private fun getPressureColor(pressureStr: String): Int {
        val pressure = pressureStr.toFloatOrNull() ?: return Color.GRAY
        return when {
            pressure < 1000 -> Color.parseColor("#FFA07A") // LightSalmon (low pressure)
            pressure in 1000.0..1020.0 -> Color.parseColor("#90EE90") // Normal
            else -> Color.parseColor("#D8BFD8") // Thistle (high pressure)
        }
    }

    private fun getHumidityColor(humidityStr: String): Int {
        val humidity = humidityStr.toFloatOrNull() ?: return Color.GRAY
        return when {
            humidity < 30 -> Color.parseColor("#F0E68C") // Khaki (dry)
            humidity in 30.0..60.0 -> Color.parseColor("#90EE90") // Comfortable
            else -> Color.parseColor("#87CEFA") // LightSkyBlue (high humidity)
        }
    }
}