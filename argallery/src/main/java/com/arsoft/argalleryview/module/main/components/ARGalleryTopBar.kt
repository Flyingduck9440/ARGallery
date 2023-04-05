package com.arsoft.argalleryview.module.main.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arsoft.argalleryview.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ARGalleryTopBar(
    title: String,
    onClose: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                textAlign = TextAlign.Center
            )
        },
        navigationIcon = {
            Icon(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .clickable { onClose() },
                painter = painterResource(R.drawable.ic_close),
                contentDescription = null
            )
        }
    )
}