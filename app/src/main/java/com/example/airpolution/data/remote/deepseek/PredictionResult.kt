package com.example.airpolution.data.remote.deepseek

sealed class PredictionResult {
    data class Success(val data: String) : PredictionResult()
    data class Error(val message: String) : PredictionResult()
}