package com.example.airpolution.ui.home.datesBuilder

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateMonth {
    @SuppressLint("ConstantLocale")
    private val apiDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
    fun getPreviousMonthRangeLegacy(): Pair<String, String> {
        val calendar = Calendar.getInstance()

        // Move to previous month
        calendar.add(Calendar.MONTH, -1)

        // Set to first day of month at 00:00:00
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val fromDate = calendar.clone() as Calendar

        // Set to last day of month at 23:59:59
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
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