package com.example.moneyflow.utils

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.random.Random


class ImageSaver {
    fun saveToInternalStorage(context: Context, bitmapImage: Bitmap): String? {
        val mypath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), Random.nextInt(10, 30).toString() + ".jpg")
        var fos: FileOutputStream? = null
        try {
            fos =
                FileOutputStream(mypath) // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 20, fos)
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
}