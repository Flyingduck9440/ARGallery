package com.arsoft.argalleryview.module.main.model

import android.net.Uri

data class Items(
    val id: String? = null,
    val fileName: String? = null,
    val dateTaken: Long? = null,
    val mimeType: String? = null,
    val uri: Uri = Uri.EMPTY,
    val isChecked: Boolean = false
)

