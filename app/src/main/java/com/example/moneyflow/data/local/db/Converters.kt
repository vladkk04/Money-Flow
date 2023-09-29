package com.example.moneyflow.data.local.db

import android.net.Uri
import androidx.core.net.toUri
import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromUri(uri: Uri?): String? {
        return uri?.toString()
    }

    @TypeConverter
    fun toUri(string: String?): Uri? {
        return string?.toUri()
    }


}