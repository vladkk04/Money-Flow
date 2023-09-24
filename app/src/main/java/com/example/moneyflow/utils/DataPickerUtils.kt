package com.example.moneyflow.utils

import android.text.format.DateUtils
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DataPickerUtils {
    fun currentDay(date: Date): String {
        return if (isToday(date)) {
            "Today"
        } else if (isTomorrow(date)) {
            "Tomorrow"
        } else if (isYesterday(date)) {
            "Yesterday"
        } else {
            SimpleDateFormat("EEE, dd/MM/yyyy", Locale.getDefault()).format(date)
        }
    }

    private fun isToday(date: Date) = DateUtils.isToday(date.time)

    private fun isYesterday(date: Date) = DateUtils.isToday(date.time + DateUtils.DAY_IN_MILLIS)

    private fun isTomorrow(date: Date) = DateUtils.isToday(date.time - DateUtils.DAY_IN_MILLIS)

    val datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText("Select dates")
        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
        .build()
}