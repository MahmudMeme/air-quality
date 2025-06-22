package com.example.airpolution.domain

import com.google.gson.Gson
import com.google.gson.JsonObject

object ParseJSON {

    private fun validString(jsonString: String): String {
        val list = jsonString.split("\n")
        val newList = list.subList(1, list.size - 1)
        val sb = StringBuilder()
        for (s in newList) {
            sb.append(s)
        }
        return sb.toString()
    }

    fun parsePredictionResponse(jsonString: String?): List<String> {
        if (jsonString.isNullOrEmpty() || jsonString.isBlank()) return emptyList()
        val s = validString(jsonString)
        val gson = Gson()
        val jsonObject = gson.fromJson(s.trimIndent(), JsonObject::class.java)
        val prediction = jsonObject.getAsJsonObject("prediction")
        val keyFactors = jsonObject.getAsJsonArray("key_factors")
        val healthImplications = jsonObject.get("health_implications").asString
        val modelNotes = jsonObject.get("model_notes").asString

        // First string - Date and measurements
        val firstString = StringBuilder().apply {
            // Add date
            appendLine("Date: ${prediction.get("date").asString}")

            // Add each measurement
            listOf("pm10", "pm25", "temperature", "humidity", "noise").forEach { key ->
                val measurement = prediction.getAsJsonObject(key)
                append("$key = ${measurement.get("value").asInt} with ${measurement.get("confidence").asInt}%, ")
                append("expected_range = from ${measurement.getAsJsonArray("expected_range")[0].asInt} ")
                append("to ${measurement.getAsJsonArray("expected_range")[1].asInt}, ")
                appendLine("trend = ${measurement.get("trend").asString}")
            }
        }.toString()

        // Second string - Key factors
        val secondString = StringBuilder("Key Factors:\n").apply {
            keyFactors.forEach { factor ->
                appendLine("- ${factor.asString}")
            }
        }.toString()

        // Third string - Health implications and model notes
        val thirdString = StringBuilder().apply {
            appendLine("Health Implications:")
            appendLine(healthImplications)
            appendLine("\nModel Notes:")
            appendLine(modelNotes)
        }.toString()

        return listOf(firstString, secondString, thirdString)
    }
}