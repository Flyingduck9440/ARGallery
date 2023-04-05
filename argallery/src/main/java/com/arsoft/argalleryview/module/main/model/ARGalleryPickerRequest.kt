package com.arsoft.argalleryview.module.main.model

sealed interface MediaType {
    object ImageOnly: MediaType
    object VideoOnly: MediaType
    object ImageAndVideo: MediaType
}

fun ARGalleryPickerRequest(
    mediaType: MediaType = MediaType.ImageOnly
) = ARGalleryPickerRequest.Builder()
    .setMediaType(mediaType)
    .build()

class ARGalleryPickerRequest internal constructor() {
    var mediaType: MediaType = MediaType.ImageOnly
        internal set

    class Builder {
        private var mediaType: MediaType = MediaType.ImageOnly

        fun setMediaType(mediaType: MediaType): Builder {
            this.mediaType = mediaType
            return this
        }

        fun build(): ARGalleryPickerRequest = ARGalleryPickerRequest().apply {
            this.mediaType = this@Builder.mediaType
        }
    }
}