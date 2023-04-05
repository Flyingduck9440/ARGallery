package com.arsoft.argalleryview.module.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arsoft.argalleryview.module.main.view.ARGalleryPickerScreen
import com.arsoft.argalleryview.module.preview.view.PreviewScreen
import com.arsoft.argalleryview.module.main.viewmodel.ARGalleryPickerEvents
import com.arsoft.argalleryview.module.main.viewmodel.ARGalleryPickerViewModel
import com.arsoft.argalleryview.ui.theme.AppTheme
import com.arsoft.argalleryview.utils.ARGalleryResult.Companion.getARGalleryResponseIntent

val font_date_header = 16.sp
val date_header_paddings = PaddingValues(
    start = 8.dp,
    top = 6.dp,
    bottom = 6.dp,
    end = 8.dp
)
val bottomSheetHeight = 70.dp
val REQUIRE_PERMISSIONS =
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO
            )
        }
        Build.VERSION.SDK_INT > Build.VERSION_CODES.P -> {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        else -> {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

object NavRoute {
    const val Gallery = "main_gallery"
    const val Preview = "preview"
}

class ARGalleryPickerActivity : ComponentActivity() {
    private lateinit var viewModel: ARGalleryPickerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ARGalleryPickerViewModel()

        viewModel.onEvent(
            ARGalleryPickerEvents.SetLimit(
                intent.getIntExtra("limit", viewModel.itemsList.size)
            )
        )

        setContent {
            val context = LocalContext.current
            val permissionsRequester = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions(),
                onResult = { result ->
                    when {
                        result.values.all { it } -> {
                            viewModel.loadImages(context)
                        }
                        else -> {
                            Toast.makeText(context, "Please allow permission in app settings.", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            )
            val navController = rememberNavController()
            val selectedItems by remember {
                derivedStateOf {
                    viewModel.itemsList.filter {
                        it.isChecked
                    }
                }
            }

            LaunchedEffect(context) {
                if (checkPermissionsReady(context)) {
                    viewModel.loadImages(context)
                } else {
                    permissionsRequester.launch(REQUIRE_PERMISSIONS)
                }
            }

            AppTheme(useDarkTheme = false) {
                NavHost(
                    navController = navController,
                    route = "root",
                    startDestination = NavRoute.Gallery
                ) {
                    composable(route = NavRoute.Gallery) {
                        ARGalleryPickerScreen(
                            data = viewModel.itemsList,
                            selectionEvents = viewModel.selectionEvent,
                            limit = viewModel.limit,
                            onEvent = viewModel::onEvent,
                            openPreview = {
                                navController.navigate(NavRoute.Preview)
                            },
                            onSetResult = {
                                setResult(RESULT_OK, getARGalleryResponseIntent(selectedItems))
                                finish()
                            },
                            onBackPressed = {
                                setResult(RESULT_CANCELED)
                                finish()
                            }
                        )
                    }
                    composable(route = NavRoute.Preview) {
                        PreviewScreen(
                            data = selectedItems,
                            onEvent = viewModel::onEvent,
                            onSetResult = {
                                setResult(RESULT_OK, getARGalleryResponseIntent(selectedItems))
                                finish()
                            },
                            onBackPressed = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}

private fun checkPermissionsReady(context: Context) =
    REQUIRE_PERMISSIONS.all { permission ->
        ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }