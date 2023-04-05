package com.arsoft.argalleryview.module.main.components

import android.text.format.DateUtils
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.arsoft.argalleryview.module.main.model.Items
import com.arsoft.argalleryview.module.main.viewmodel.ARGalleryPickerEvents
import java.util.*

@Composable
fun ImageListBuilder(
    data: List<Items>,
    onEvent: (ARGalleryPickerEvents) -> Unit
) {
    val context = LocalContext.current
    var dateTaken by remember {
        mutableStateOf(0L)
    }
    LazyVerticalGrid(
        columns = GridCells.Adaptive(90.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        data.forEachIndexed { _, items ->
            if (DateUtils.isToday(items.dateTaken?.times(1000L) ?: 0)
                && dateTaken == 0L
            ) {
                item(span = { GridItemSpan(this.maxLineSpan) }) {
                    ItemsDateHeader(text = "Today")
                }
                items.dateTaken?.let {
                    dateTaken = it
                }
            }

            val baseDate = Date(dateTaken * 1000L)
            val compareDate = Date(items.dateTaken?.times(1000L) ?: 0)

            if (!org.apache.commons.lang3.time.DateUtils.isSameDay(baseDate, compareDate)) {
                item(span = { GridItemSpan(this.maxLineSpan) }) {
                    ItemsDateHeader(
                        text = DateUtils.formatDateTime(
                            context,
                            items.dateTaken?.times(1000L) ?: 0,
                            DateUtils.FORMAT_ABBREV_ALL
                        )
                    )
                }
                items.dateTaken?.let {
                    dateTaken = it
                }
            }

            item(key = items.hashCode()) {
                ItemsImageBuilder(
                    id = items.id!!,
                    path = items.uri.path,
                    isChecked = items.isChecked,
                    onClick = { id, isChecked ->
                        onEvent(ARGalleryPickerEvents.OnCheckedChanged(id, isChecked))
                    }
                )
            }
        }
    }
}