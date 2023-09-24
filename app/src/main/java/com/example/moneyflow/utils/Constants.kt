package com.example.moneyflow.utils

import android.Manifest


object Constants {
    const val patternFormatAmount = "#,###.##"
    val permissionToRequest = arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.CAMERA
    )
}