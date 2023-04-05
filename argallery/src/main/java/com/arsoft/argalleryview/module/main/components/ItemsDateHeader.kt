package com.arsoft.argalleryview.module.main.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arsoft.argalleryview.module.main.date_header_paddings
import com.arsoft.argalleryview.module.main.font_date_header

@Composable
fun ItemsDateHeader(
    text: String
) {
    Text(
        modifier = Modifier.padding(date_header_paddings),
        text = text,
        fontSize = font_date_header
    )
}