package com.example.moneyflow.utils

fun interface OnPositiveCallbacks {
    operator fun invoke(date: Long)
}