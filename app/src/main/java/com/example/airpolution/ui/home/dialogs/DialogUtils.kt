package com.example.airpolution.ui.home.dialogs

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.example.airpolution.R
import com.example.airpolution.domain.CardAirMeasurementDisplay
import java.util.Locale

object DialogUtils {
    fun showMeasurementInfoDialog(
        context: Context,
        measurement: CardAirMeasurementDisplay,
    ) {
        val dialog = Dialog(context).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_measurement_info)
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            window?.attributes?.windowAnimations = R.style.DialogAnimation
            setCancelable(true)
        }

        val title = dialog.findViewById<TextView>(R.id.title)
        val content = dialog.findViewById<TextView>(R.id.content)
        val closeButton = dialog.findViewById<Button>(R.id.closeButton)

        when (measurement.type.lowercase(Locale.getDefault())) {
            "temperature" -> {
                title.text = context.getString(R.string.temperature_title)
                content.text = context.getString(R.string.temperature_info)
            }

            "pm10" -> {
                title.text = context.getString(R.string.pm10_title)
                content.text = context.getString(R.string.pm10_info)
            }

            "pm25" -> {
                title.text = context.getString(R.string.pm25_title)
                content.text = context.getString(R.string.pm25_info)
            }

            "no2" -> {
                title.text = context.getString(R.string.no2_title)
                content.text = context.getString(R.string.no2_info)
            }

            "o3" -> {
                title.text = context.getString(R.string.o3_title)
                content.text = context.getString(R.string.o3_info)
            }

            "humidity" -> {
                title.text = context.getString(R.string.humidity_title)
                content.text = context.getString(R.string.humidity_info)
            }

            "pressure" -> {
                title.text = context.getString(R.string.pressure_title)
                content.text = context.getString(R.string.pressure_info)
            }

            "noise" -> {
                title.text = context.getString(R.string.noise_title)
                content.text = context.getString(R.string.noise_info)
            }
        }

        val currentValueText = "\n\nCurrent Value: ${measurement.displayText}"
        content.append(currentValueText)

        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}