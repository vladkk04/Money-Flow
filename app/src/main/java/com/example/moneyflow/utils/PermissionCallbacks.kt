package com.example.moneyflow.utils

interface PermissionCallbacks {
    fun onGranted()

    fun onDenied()

    fun onShowRationale()

    fun onNeverAskAgain()

}