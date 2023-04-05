package com.arsoft.argalleryview.utils

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import java.io.File

fun File.getContentUri(context: Context, onResult: (Uri) -> Unit) {
    MediaScannerConnection.scanFile(
        context,
        arrayOf(this.absolutePath),
        null
    ) { _, uri ->
        onResult(uri)
    }
}