package com.arsoft.argalleryview.module.preview.view

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arsoft.argalleryview.R
import com.arsoft.argalleryview.module.main.model.Items
import com.arsoft.argalleryview.module.main.viewmodel.ARGalleryPickerEvents
import com.arsoft.argalleryview.module.preview.components.PreviewBottomSheet
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalGlideComposeApi::class
)
@Composable
fun PreviewScreen(
    data: List<Items>,
    onEvent: (ARGalleryPickerEvents) -> Unit,
    onBackPressed: () -> Unit,
    onSetResult: () -> Unit
) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    var pageIndex by remember {
        mutableStateOf(0)
    }

    LaunchedEffect(data.size) {
        if (data.isEmpty()) {
            onBackPressed()
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        pageIndex = pagerState.currentPage
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = Color.Black,
                    actionIconContentColor = Color.White,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            data.takeIf { it.isNotEmpty() }?.let {
                PreviewBottomSheet(
                    selectedItemsCount = data.size,
                    id = data[pageIndex].id!!,
                    isSelected = data[pageIndex].isChecked,
                    onCheckedChange = { id, isChecked ->
                        coroutineScope.launch {
                            if (pagerState.canScrollBackward) {
                                pagerState.animateScrollToPage(
                                    pageIndex - 1,
                                    animationSpec = spring(stiffness = Spring.StiffnessHigh)
                                )
                            }
                            onEvent(ARGalleryPickerEvents.OnCheckedChanged(id, isChecked))
                        }
                    },
                    onAdd = onSetResult
                )
            }
        },
        containerColor = Color.Black
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            HorizontalPager(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 100.dp, bottom = 100.dp),
                state = pagerState,
                pageCount = data.size,
                verticalAlignment = Alignment.CenterVertically
            ) { index ->
                Column(modifier = Modifier.fillMaxSize()) {
                    val item = data[index]
                    GlideImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(0.9f),
                        model = item.uri.path,
                        contentDescription = null
                    ) { it.centerInside() }
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.1f),
                        text = "${index + 1} / ${data.size}",
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    PreviewScreen(
        data = ArrayList(),
        onEvent = {},
        onBackPressed = {},
        onSetResult = {}
    )
}