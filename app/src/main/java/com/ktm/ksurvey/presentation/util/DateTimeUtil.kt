package com.ktm.ksurvey.presentation.util

import android.icu.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateTimeUtil {

    fun getFormatedDate(): String {
        return getFormatedDate(Calendar.getInstance())
    }

    fun getFormatedDate(calendar: Calendar): String {
        val formater = SimpleDateFormat("EEEE, MMM d", Locale.getDefault())
        return formater.format(calendar.time)
    }
}