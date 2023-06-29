package com.hermanowicz.wirelesswhisper.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

class DateFormatter {
    companion object {
        @SuppressLint("SimpleDateFormat")
        fun getFullDate(timestamp: Long): String {
            val date = Date(timestamp)
            val format = SimpleDateFormat("HH:mm - dd MMM yyyy")
            return format.format(date)
        }
    }
}
