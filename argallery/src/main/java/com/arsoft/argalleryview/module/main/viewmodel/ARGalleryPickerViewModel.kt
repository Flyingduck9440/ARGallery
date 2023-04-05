package com.arsoft.argalleryview.module.main.viewmodel

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsoft.argalleryview.module.main.model.Items
import com.arsoft.argalleryview.utils.getContentUri
import com.arsoft.argalleryview.utils.getCursorLong
import com.arsoft.argalleryview.utils.getCursorString
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import java.io.File

sealed class ARGalleryPickerEvents {
    data class OnCheckedChanged(val id: String, val isChecked: Boolean) : ARGalleryPickerEvents()
    data class SetLimit(val limit: Int) : ARGalleryPickerEvents()
}

sealed class SelectionEvents {
    object Success : SelectionEvents()
    object Error : SelectionEvents()
}

class ARGalleryPickerViewModel : ViewModel() {

    var itemsList = mutableStateListOf<Items>()
        private set
    var limit by mutableStateOf(itemsList.size)
        private set
    var selectionEvent = Channel<SelectionEvents>()
        private set

    private val listItems: ArrayList<Items> = ArrayList()

    fun onEvent(event: ARGalleryPickerEvents) {
        when (event) {
            is ARGalleryPickerEvents.OnCheckedChanged -> {
                val currentSelected = itemsList.filter { it.isChecked }.size
                if ((currentSelected+1) > limit && event.isChecked) {
                    viewModelScope.launch {
                        selectionEvent.send(SelectionEvents.Error)
                    }
                } else {
                    viewModelScope.launch {
                        selectionEvent.send(SelectionEvents.Success)
                    }
                    val index = itemsList.indexOf(itemsList.find { it.id == event.id })
                    itemsList.set(
                        index = index,
                        element = itemsList[index].copy(
                            isChecked = event.isChecked
                        )
                    )
                }
            }
            is ARGalleryPickerEvents.SetLimit -> {
                limit = event.limit
            }
        }
    }

    internal fun loadImages(context: Context) {
        try {
            context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                arrayOf(
                    MediaStore.MediaColumns._ID,
                    MediaStore.MediaColumns.DATE_MODIFIED,
                    MediaStore.MediaColumns.MIME_TYPE,
                    MediaStore.MediaColumns.DATA,
                    MediaStore.MediaColumns.DISPLAY_NAME
                ),
                null,
                null,
                MediaStore.MediaColumns.DATE_MODIFIED + " DESC"
            )?.use { cursor ->
                while (cursor.moveToNext()) {
                    listItems.add(
                        Items(
                            id = cursor.getCursorString(MediaStore.MediaColumns._ID),
                            fileName = cursor.getCursorString(MediaStore.MediaColumns.DISPLAY_NAME),
                            dateTaken = cursor.getCursorLong(MediaStore.MediaColumns.DATE_MODIFIED),
                            mimeType = cursor.getCursorString(MediaStore.MediaColumns.MIME_TYPE),
                            uri = cursor.getCursorString(MediaStore.MediaColumns.DATA)?.toUri()
                                ?: Uri.EMPTY
                        )
                    )
                }
                itemsList.addAll(listItems)
            }
        } catch (e: Exception) {
            println(e)
        }
    }

    private fun updateItemsFromCursor(context: Context, cursor: Cursor) {
        val id = cursor.getCursorString(MediaStore.MediaColumns._ID)
        val path = cursor.getCursorString(MediaStore.MediaColumns.DATA)
        path.takeIf { it != null && it.isNotEmpty() }?.let {
            val file = File(it)
            if (file.exists()) {
                file.getContentUri(context) { uri ->
                    println(uri)
                }
            }
        }
    }
}
