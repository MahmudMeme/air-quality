package com.example.airpolution.domain

import android.graphics.Path

data class CardAirMeasurementDisplay(
    val type:String,
    val value: Int,
    val displayText:String,
    val color: Int,
    val imagePath: Path
)
