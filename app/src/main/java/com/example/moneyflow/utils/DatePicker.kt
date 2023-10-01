package com.example.moneyflow.utils

import android.text.format.DateUtils
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Fragment.showDatePicker(
    title: String = "Select Date",
    selection: Long = MaterialDatePicker.todayInUtcMilliseconds(),
    onPositiveCallback: OnPositiveCallbacks
) {
    MaterialDatePicker.Builder.datePicker().setTitleText(title)
        .setSelection(selection)
        .build().apply {
            this.addOnPositiveButtonClickListener { date ->
                onPositiveCallback(date)
            }
            this.show(this@showDatePicker.parentFragmentManager, "date_picker")
        }
}

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