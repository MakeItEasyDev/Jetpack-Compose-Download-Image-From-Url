package com.jetpack.downloadimagefromurl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

class DownloadFileWorker(
    context: Context,
    params: WorkerParameters
): CoroutineWorker(context, params) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val imageUrl = inputData.getString("url")
        val destination = inputData.getString("destination")
        try {
            if (destination == null || imageUrl == null) {
                return@withContext Result.failure()
            }

            val outputStream = applicationContext.contentResolver?.openOutputStream(Uri.parse(destination))
            var bitmap: Bitmap? = null

            try {
                val inputStream = URL(imageUrl).openStream()
                bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            try {
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }

            outputStream?.flush()
            outputStream?.close()
            return@withContext Result.success()
        } catch (ex: Exception) {
            return@withContext Result.failure()
        }
    }
}
























