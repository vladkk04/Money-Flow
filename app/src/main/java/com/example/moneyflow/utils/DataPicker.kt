package com.example.moneyflow.utils

import android.text.format.DateUtils
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DataPicker {
    fun showDatePicker(
        fragmentManager: FragmentManager,
        title: String = "Select Date",
        onPositiveButtonClickCallBack: (date: Long) -> Unit = {},
    ) {
        val dataPicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(title)
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        dataPicker.addOnPositiveButtonClickListener { date ->
            onPositiveButtonClickCallBack(date)
        }

        if (dataPicker.showsDialog) {
            dataPicker.show(fragmentManager, "date_picker")
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
}