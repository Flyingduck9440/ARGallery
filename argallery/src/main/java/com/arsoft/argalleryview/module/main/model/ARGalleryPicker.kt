package com.arsoft.argalleryview.module.main.model

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import com.arsoft.argalleryview.module.main.ARGalleryPickerActivity

open class ARGalleryPicker(
    private val maxItems: Int = Int.MAX_VALUE
) : ActivityResultContract<ARGalleryPickerRequest, List<Uri>>() {

    init {
        require(
            maxItems > 0
        )
    }

    internal companion object {
        internal fun Intent.getClipDataUris(): List<Uri> {
            val resultSet = LinkedHashSet<Uri>()
            data?.let { data ->
                resultSet.add(data)
            }
            val clipData = clipData
            if (clipData == null && resultSet.isEmpty()) {
                return emptyList()
            } else if (clipData != null) {
                for (i in 0 until clipData.itemCount) {
                    val uri = clipData.getItemAt(i).uri
                    if (uri != null) {
                        resultSet.add(uri)
                    }
                }
            }
            return ArrayList(resultSet)
        }

        internal fun getMimeType(mediaType: MediaType): String? {
            return when (mediaType) {
                MediaType.ImageAndVideo -> null
                MediaType.ImageOnly -> "image/*"
                MediaType.VideoOnly -> "video/*"
            }
        }
    }

    final override fun createIntent(context: Context, input: ARGalleryPickerRequest): Intent {
        return Intent(context, ARGalleryPickerActivity::class.java).apply {
            type = getMimeType(input.mediaType)
            putExtra("limit", maxItems)
        }
    }

    final override fun parseResult(resultCode: Int, intent: Intent?): List<Uri> {
        return intent.takeIf { resultCode == Activity.RESULT_OK }?.getClipDataUris() ?: emptyList()
    }

}