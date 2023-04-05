package com.arsoft.argalleryview.module.main.model

sealed interface MediaType {
    object ImageOnly: MediaType
    object VideoOnly: MediaType
    object ImageAndVideo: MediaType
}

fun ARGalleryPickerRequest(
    mediaType: MediaType = MediaType.ImageOnly,
    maxItems: Int = Int.MAX_VALUE
) = ARGalleryPickerRequest.Builder()
    .setMediaType(mediaType)
    .build()

class ARGalleryPickerRequest internal constructor() {
    var mediaType: MediaType = MediaType.ImageOnly
        internal set
//    var maxItems: Int = Int.MAX_VALUE
//        internal set

    class Builder {
        private var mediaType: MediaType = MediaType.ImageOnly
//        private var maxItems: Int = Int.MAX_VALUE

        fun setMediaType(mediaType: MediaType): Builder {
            this.mediaType = mediaType
            return this
        }

//        fun setMaxItems(maxItems: Int): Builder {
//            this.maxItems = maxItems
//            return this
//        }

        fun build(): ARGalleryPickerRequest = ARGalleryPickerRequest().apply {
            this.mediaType = this@Builder.mediaType
//            this.maxItems = this@Builder.maxItems
        }
    }
}