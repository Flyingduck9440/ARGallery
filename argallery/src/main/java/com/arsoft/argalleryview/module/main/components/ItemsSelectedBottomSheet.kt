package com.arsoft.argalleryview.module.main.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.arsoft.argalleryview.R

@Composable
fun ItemSelectedBottomSheet(
    modifier: Modifier,
    selectedItemsCount: Int,
    onViewSelected: () -> Unit,
    onAdd: () -> Unit
) {
    var bottomBarHeight by remember {
        mutableStateOf(0)
    }

    AnimatedVisibility(
        modifier = modifier.then(
            Modifier.onGloballyPositioned {
                bottomBarHeight = it.size.height
            }
        ),
        visible = selectedItemsCount > 0,
        enter = slideInVertically(
            initialOffsetY = {
                bottomBarHeight
            }
        ),
        exit = slideOutVertically(
            targetOffsetY = { bottomBarHeight }
        )
    ) {
        Card(
            modifier = Modifier.pointerInput(Unit) {
                detectTapGestures {
                    onViewSelected()
                }
            },
            shape = RoundedCornerShape(
                topStart = 4.dp,
                topEnd = 4.dp
            )
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.padding(start = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(
                        12.dp,
                        alignment = Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_image),
                        contentDescription = null
                    )
                    Text(text = "View selected")
                }
                Button(onClick = onAdd) {
                    Text(text = "Add ($selectedItemsCount)")
                }
            }
        }
    }
}