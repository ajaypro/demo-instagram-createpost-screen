package com.android.instagram.utils.common

import android.content.Context
import android.graphics.BitmapFactory
import com.android.instagram.utils.log.Logger
import com.mindorks.paracamera.Camera
import com.mindorks.paracamera.Utils
import java.io.*

/**
 * Created by Ajay Deepak on 27-07-2019.
 */

object FileUtils {

    fun getDirectory(context: Context, dirName: String): File {
        val file = File(context.filesDir.path + File.separator + dirName)
        if (!file.exists()) file.mkdir()
        return file
    }

    fun saveInputStreamToFile(input: InputStream, directory: File, imageName: String, height: Int): File? {

        Logger.d("DEBUG", directory.toString())
        val temp = File(directory.path + File.separator + "temp\$file\$for\$processing")
        input.use {
            val finalFile = File(directory.path + File.separator + imageName + ".${Camera.IMAGE_JPG}")
            Logger.d("DEBUG", finalFile.toString())
            val outputStream = FileOutputStream(temp)
            try {
                val buffer = ByteArray(4 * 1024)
                var readBuffer = input.read(buffer)
                Logger.d("DEBUG", readBuffer.toString())
                while (readBuffer != -1) {
                    outputStream.write(buffer, 0, readBuffer)
                    readBuffer = input.read(buffer)

                }
                outputStream.flush()
                Utils.saveBitmap(Utils.decodeFile(temp, height), finalFile.path, Camera.IMAGE_JPG, 80)
                Logger.d("DEBUG", finalFile.toString())
                return finalFile
            } finally {
                outputStream.close()
                temp.delete()
            }
        }

    }

    // Get image size from file stream without allocating memory
    fun getImageSize(file: File): Pair<Int, Int>? {

        return try {
            val bitmapFactory = BitmapFactory.Options()
            bitmapFactory.inJustDecodeBounds = true // this function does not allocate memory for bitmap
            BitmapFactory.decodeStream(FileInputStream(file), null, bitmapFactory)

            Logger.d("DEBUG", Pair(bitmapFactory.outWidth, bitmapFactory.outHeight).toString())
            Pair(bitmapFactory.outWidth, bitmapFactory.outHeight)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        }
    }
}