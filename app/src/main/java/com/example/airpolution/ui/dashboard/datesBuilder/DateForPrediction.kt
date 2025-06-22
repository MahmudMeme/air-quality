package com.example.airpolution.ui.dashboard.datesBuilder

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateForPrediction {
    @SuppressLint("ConstantLocale")
    private val apiDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())

    fun getLast30DaysRange(): Pair<String, String> {
        val calendar = Calendar.getInstance()

        // End date (today at 23:59:59)
        val toDate = calendar.clone() as Calendar
        toDate.set(Calendar.HOUR_OF_DAY, 23)
        toDate.set(Calendar.MINUTE, 59)
        toDate.set(Calendar.SECOND, 59)

        // Start date (30 days ago at 00:00:00)
        calendar.add(Calendar.DATE, -30)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val fromDate = calendar

        // Format dates and encode plus signs
        val fromStr = apiDateFormat.format(fromDate.time).replace("+", "%2b")
        val toStr = apiDateFormat.format(toDate.time).replace("+", "%2b")

        return Pair(fromStr, toStr)
    }
    fun getDateRangeForLastNDays(days: Int): Pair<String, String> {
        val calendar = Calendar.getInstance()

        // End date (today at 23:59:59)
        val toDate = calendar.clone() as Calendar
        toDate.set(Calendar.HOUR_OF_DAY, 23)
        toDate.set(Calendar.MINUTE, 59)
        toDate.set(Calendar.SECOND, 59)

        // Start date (N days ago at 00:00:00)
        calendar.add(Calendar.DATE, -days)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val fromDate = calendar

        // Format dates and encode plus signs
        val fromStr = apiDateFormat.format(fromDate.time).replace("+", "%2b")
        val toStr = apiDateFormat.format(toDate.time).replace("+", "%2b")

        return Pair(fromStr, toStr)
    }
}