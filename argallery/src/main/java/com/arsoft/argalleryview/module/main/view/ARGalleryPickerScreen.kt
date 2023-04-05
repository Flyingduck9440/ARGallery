package com.arsoft.argalleryview.module.main.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.arsoft.argalleryview.R
import com.arsoft.argalleryview.module.main.bottomSheetHeight
import com.arsoft.argalleryview.module.main.components.ARGalleryTopBar
import com.arsoft.argalleryview.module.main.components.ImageListBuilder
import com.arsoft.argalleryview.module.main.components.ItemSelectedBottomSheet
import com.arsoft.argalleryview.module.main.model.Items
import com.arsoft.argalleryview.module.main.viewmodel.ARGalleryPickerEvents
import com.arsoft.argalleryview.module.main.viewmodel.SelectionEvents
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ARGalleryPickerScreen(
    data: List<Items>,
    selectionEvents: Channel<SelectionEvents>,
    limit: Int,
    onEvent: (ARGalleryPickerEvents) -> Unit,
    openPreview: () -> Unit,
    onSetResult: () -> Unit,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    val selectedItems by remember {
        derivedStateOf {
            data.filter { it.isChecked }
        }
    }
    val errorSelectionToast = remember {
        Toast.makeText(
            context,
            context.resources.getQuantityString(R.plurals.item_selection, limit, limit),
            Toast.LENGTH_SHORT
        )
    }

    LaunchedEffect(context) {
        selectionEvents.receiveAsFlow().collect { event ->
            when (event) {
                SelectionEvents.Error -> {
                    errorSelectionToast.show()
                }
                SelectionEvents.Success -> {
                    errorSelectionToast.cancel()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            ARGalleryTopBar(
                title = "${selectedItems.size} of $limit Selected",
                onClose = onBackPressed
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            ImageListBuilder(
                data = data,
                onEvent = onEvent
            )
            ItemSelectedBottomSheet(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(bottomSheetHeight),
                selectedItemsCount = selectedItems.size,
                onViewSelected = openPreview,
                onAdd = onSetResult
            )
        }
    }
}