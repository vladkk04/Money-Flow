package com.example.moneyflow.utils

import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.File

class ImageStorageManager {
    fun getAppSpecificAlbumStorageDir(context: Context, albumName: String): File {
        val file = File(
            context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES
            ), albumName
        )
        Log.d("d", file.absolutePath)
        if (!file.mkdirs()) {
            Log.d("d", "Directory not created")
        }
        return file
    }

}