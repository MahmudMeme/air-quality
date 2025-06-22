package com.example.airpolution.domain

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object TableForPrediction {
    fun buildPredictionPrompt(
        listPm10: List<Int?>,
        listPm25: List<Int?>,
        listTemperature: List<Int?>,
        listHumidity: List<Int?>,
        listNoise: List<Int?>,
    ): String {
        // Create date labels (assuming most recent date is yesterday)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dates = List(30) { i ->
            val cal = Calendar.getInstance()
            cal.add(Calendar.DATE, -29 + i) // Last 30 days
            dateFormat.format(cal.time)
        }

        // Build a structured data table
        val dataTable = buildString {
            appendLine("DATE\t\tPM2.5\tPM10\tTEMP\tHUM\tNOISE")
            appendLine("--------------------------------------------------")
            dates.forEachIndexed { index, date ->
                append(date)
                append("\t")
                append(listPm25.getOrNull(index) ?: "null")
                append("\t")
                append(listPm10.getOrNull(index) ?: "null")
                append("\t")
                append(listTemperature.getOrNull(index) ?: "null")
                append("\t")
                append(listHumidity.getOrNull(index) ?: "null")
                append("\t")
                append(listNoise.getOrNull(index) ?: "null")
                appendLine()
            }
        }

        // Current weather context (example - replace with actual data if available)
        val currentWeather = """
        Current Weather Context:
        - Temperature: ${listTemperature.lastOrNull()}°C
        - Humidity: ${listHumidity.lastOrNull()}%
        - Recent Noise Levels: ${listNoise.lastOrNull()} dB
    """.trimIndent()

        return """
        You are an expert environmental data scientist with specialization in air quality forecasting. 
        Analyze the following time-series data and predict PM2.5 and PM10 levels for tomorrow.

        DATA TABLE (last 30 days):
        $dataTable

        $currentWeather

        ANALYSIS INSTRUCTIONS:
        1. Identify trends, weekly patterns, and correlations between:
           - PM2.5/PM10 vs Temperature/Humidity
           - Weekday vs Weekend patterns
           - Any apparent spikes/drops and possible causes
        2. Consider typical seasonal patterns for this location
        3. Account for any visible lag effects (e.g., pollution from 2 days ago affecting today)

        RESPONSE FORMAT (strict JSON):
        {
            "prediction": {
                "date": "tomorrow's date in YYYY-MM-DD",
                "pm10": {
                    "value": predictedValue,
                    "confidence": 0-100,
                    "trend": "up/down/stable",
                    "expected_range": [min, max]
                },
                "pm25": {
                    "value": predictedValue,
                    "confidence": 0-100,
                    "trend": "up/down/stable",
                    "expected_range": [min, max]
                },
                "temperature": {
                    "value": predictedValue,
                    "confidence": 0-100,
                    "trend": "up/down/stable",
                    "expected_range": [min, max]
                },
                "humidity": {
                    "value": predictedValue,
                    "confidence": 0-100,
                    "trend": "up/down/stable",
                    "expected_range": [min, max]
                },
                "noise": {
                    "value": predictedValue,
                    "confidence": 0-100,
                    "trend": "up/down/stable",
                    "expected_range": [min, max]
                }
            },
            "key_factors": ["list", "of", "main", "factors"],
            "health_implications": "brief assessment",
            "model_notes": "any assumptions or limitations"
        }

        IMPORTANT:
        - Provide numerical predictions even if confidence is moderate
        - Highlight any concerning levels (PM2.5 > 35 or PM10 > 50)
        - If data has missing values, state how you compensated
    """.trimIndent()
    }
}