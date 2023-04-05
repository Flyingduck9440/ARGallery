package com.arsoft.argalleryview.utils

import android.database.Cursor

fun Cursor.getCursorString(columnName: String): String? {
    val index = this.getColumnIndex(columnName)
    return this.getString(index).takeIf { index != -1 }
}

fun Cursor.getCursorLong(columnName: String): Long {
    val index = this.getColumnIndex(columnName)
    if (index == -1) return -1

    val value = this.getString(index) ?: return -1
    return value.toLongOrNull() ?: -1
}