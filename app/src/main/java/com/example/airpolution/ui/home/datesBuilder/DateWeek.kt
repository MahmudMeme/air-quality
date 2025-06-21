package com.example.airpolution.ui.home.datesBuilder

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateWeek {
    @SuppressLint("ConstantLocale")
    private val apiDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())

    fun getPreviousWeekRangeLegacy(): Pair<String, String> {
        val calendar = Calendar.getInstance()

        // Move to previous week
        calendar.add(Calendar.WEEK_OF_YEAR, -1)

        // Set to Monday of that week
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val fromDate = calendar.clone() as Calendar

        // Set to Sunday of that week
        calendar.add(Calendar.DAY_OF_WEEK, 6)
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        val toDate = calendar.clone() as Calendar

        // Format dates and encode plus signs
        val fromStr = apiDateFormat.format(fromDate.time).replace("+", "%2b")
        val toStr = apiDateFormat.format(toDate.time).replace("+", "%2b")

        return Pair(fromStr, toStr)
    }
}