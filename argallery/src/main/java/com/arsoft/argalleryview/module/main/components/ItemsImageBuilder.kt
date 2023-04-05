package com.arsoft.argalleryview.module.main.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.arsoft.argalleryview.R

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ItemsImageBuilder(
    id: String,
    path: String?,
    isChecked: Boolean,
    onClick: (id: String, isChecked: Boolean) -> Unit
) {
    val scale by animateFloatAsState(
        if (isChecked) 0.9f else 1f
    )
    Box(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .height(90.dp)
            .scale(scale)
            .clickable {
                onClick(
                    id,
                    isChecked.not()
                )
            }
    ) {
        Card(shape = RoundedCornerShape(CornerSize(2.dp))) {
            GlideImage(
                model = path,
                contentDescription = null
            ) {
                it.centerCrop()
                    .placeholder(R.drawable.lottie_img_loading)
                    .load(path)
            }
        }
        Checkbox(
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(32.dp),
            checked = isChecked,
            onCheckedChange = null
        )
    }
}