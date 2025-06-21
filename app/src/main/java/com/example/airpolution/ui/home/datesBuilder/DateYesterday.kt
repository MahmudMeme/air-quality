package com.example.airpolution.ui.home.datesBuilder

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateYesterday {
    @SuppressLint("ConstantLocale")
    private val apiDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())

    fun getYesterdayDateRange(amount: Int): Pair<String, String> {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, amount) // Go back one day to get yesterday

        // Set to start of yesterday (00:00:00)
        val fromDate = calendar.clone() as Calendar
        fromDate.set(Calendar.HOUR_OF_DAY, 0)
        fromDate.set(Calendar.MINUTE, 0)
        fromDate.set(Calendar.SECOND, 0)

        // Set to end of yesterday (23:59:59)
        val toDate = calendar.clone() as Calendar
        toDate.set(Calendar.HOUR_OF_DAY, 23)
        toDate.set(Calendar.MINUTE, 59)
        toDate.set(Calendar.SECOND, 59)

        // Format dates and encode plus signs
        val fromStr = apiDateFormat.format(fromDate.time).replace("+", "%2b")
        val toStr = apiDateFormat.format(toDate.time).replace("+", "%2b")

        return Pair(fromStr, toStr)
    }
}