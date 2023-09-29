package com.example.moneyflow.utils

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class ImageStorageManager {
    fun saveToInternalStorage(context: Context, bitmapImage: Bitmap): String? {
        val cw =
            ContextWrapper(context.applicationContext)
        val mypath = File(getAppSpecificAlbumStorageDir(context, "gg"), "hello.png")
        var fos: FileOutputStream? = null
        try {
            fos =
                FileOutputStream(mypath) // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return mypath.absolutePath
    }

    private fun getAppSpecificAlbumStorageDir(context: Context, albumName: String): File {
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), albumName)
        if (!file.mkdirs()) {
            Log.e("d", "Directory not created")
        }
        return file
    }

}