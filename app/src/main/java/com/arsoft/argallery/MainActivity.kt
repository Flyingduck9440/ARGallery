package com.arsoft.argallery

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import com.arsoft.argalleryview.module.main.model.ARGalleryPicker
import com.arsoft.argalleryview.module.main.model.ARGalleryPickerRequest

class MainActivity : ComponentActivity() {
    private lateinit var galleryResultLauncher: ActivityResultLauncher<ARGalleryPickerRequest>
    private lateinit var btnOpenCamera: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupResultLauncher()

        btnOpenCamera = findViewById(R.id.btn_open_camera)

        btnOpenCamera.setOnClickListener {
            galleryResultLauncher.launch(ARGalleryPickerRequest())
        }
    }

    private fun setupResultLauncher() {
        galleryResultLauncher = registerForActivityResult(ARGalleryPicker(5)) { uris ->
            when {
                uris.isNotEmpty() -> {
                    Log.e("DEBUG", uris.toString())
                }
            }
        }
    }
}