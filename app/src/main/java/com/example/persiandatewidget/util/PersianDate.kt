package com.example.persiandatewidget.util

import android.icu.text.DateFormat
import android.icu.text.NumberFormat
import android.icu.util.Calendar
import android.icu.util.ULocale
import java.util.Locale

class PersianDate {

    val dayOfMonth: String
    val monthName: String

    init {
        val deviceLocale = Locale.getDefault()
        val languageTag = deviceLocale.language
        val persianCalendarLocale = ULocale("${languageTag}@calendar=persian")
        val calendar = Calendar.getInstance(persianCalendarLocale)
        val dayOfMonthValue = calendar.get(Calendar.DAY_OF_MONTH)
        val numberFormat = NumberFormat.getInstance(persianCalendarLocale)
        dayOfMonth = numberFormat.format(dayOfMonthValue)
        val monthFormat = DateFormat.getInstanceForSkeleton("MMMM", persianCalendarLocale)
        monthName = monthFormat.format(calendar.time)
    }
}

