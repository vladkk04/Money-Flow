package com.example.moneyflow.domain.model

interface PermissionsMessageProvider {
    fun getMessage(isPermanentlyDeclined: Boolean): String
}