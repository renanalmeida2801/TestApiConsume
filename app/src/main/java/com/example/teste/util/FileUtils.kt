package com.example.teste.util

import android.content.Context
import android.net.Uri
import java.io.File

object FileUtils {
    fun uriToFile(context: Context, uri:Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri) ?: throw IllegalArgumentException("Invalid URI")
        val file = File(context.cacheDir, "temp_video.mp4")
        file.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }
        return file
    }
}