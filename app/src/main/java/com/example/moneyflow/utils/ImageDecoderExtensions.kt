package com.example.moneyflow.utils

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build

fun convertUriToBitmap(contentResolver: ContentResolver, setTargetSampleSize: Int = 1, uri: Uri): Bitmap {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        return ImageDecoder.decodeBitmap(
            ImageDecoder.createSource(contentResolver, uri)
        ) { decoder, _, _ ->
            decoder.setTargetSampleSize(setTargetSampleSize)
        }
    } else {
        TODO("VERSION.SDK_INT < P")
    }
}

