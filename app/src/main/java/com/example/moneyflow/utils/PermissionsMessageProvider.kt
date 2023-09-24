package com.example.moneyflow.utils

interface PermissionsMessageProvider {
    fun getMessage(isPermanentlyDeclined: Boolean): String
}