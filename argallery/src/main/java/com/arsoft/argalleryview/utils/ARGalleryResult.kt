package com.arsoft.argalleryview.utils

import android.content.ClipData
import android.content.Intent
import android.net.Uri
import com.arsoft.argalleryview.module.main.model.Items

class ARGalleryResult {

    companion object {
        fun getARGalleryResponseIntent(
            selectedItems: List<Items>
        ) : Intent {
            val selectedUris = getUrisForEachItems(selectedItems)
            val intent = Intent()

            val clipData = ClipData(
                null,
                arrayOf("image/*"),
                ClipData.Item(selectedUris[0])
            )
            selectedUris.forEachIndexed { index, uri ->
                if (index > 0) {
                    clipData.addItem(ClipData.Item(uri))
                }
            }
            intent.clipData = clipData
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            return intent
        }

        private fun getUrisForEachItems(selectedItems: List<Items>): List<Uri> {
            return selectedItems.map { it.uri }
        }
    }
}