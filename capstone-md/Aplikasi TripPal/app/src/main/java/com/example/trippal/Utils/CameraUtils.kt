package com.example.trippal.Utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Locale

private val FILE_FORMAT = "dd-MMM-yyyy"

val timestamp: String = SimpleDateFormat(
    FILE_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

fun createCustomTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return  File.createTempFile(timestamp, ".jpg", storageDir)
}

fun uriToFile(selectedImage: Uri, context: Context): File{
    val contentResolver: ContentResolver = context.contentResolver
    val myfile = createCustomTempFile(context)

    val inputStream = contentResolver.openInputStream(selectedImage) as InputStream
    val outputStream: OutputStream = FileOutputStream(myfile)
    val buf = ByteArray(1024)
    var len: Int
    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)

    outputStream.close()
    inputStream.close()

    return myfile
}