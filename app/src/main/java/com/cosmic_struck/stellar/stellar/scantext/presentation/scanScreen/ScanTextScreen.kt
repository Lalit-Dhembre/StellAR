package com.cosmic_struck.stellar.stellar.scantext.presentation.scanScreen

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.cosmic_struck.stellar.stellar.scantext.presentation.scanScreen.components.CameraScanCaption
import com.cosmic_struck.stellar.stellar.scantext.presentation.scanScreen.components.CameraScanText
import com.cosmic_struck.stellar.stellar.scantext.presentation.scanScreen.components.RectangleFrame
import com.cosmic_struck.stellar.stellar.scantext.presentation.scanScreen.components.ScanButtonScanText
import com.cosmic_struck.stellar.stellar.scantext.presentation.scanScreen.components.TopBarScanTextBook
import com.cosmic_struck.stellar.ui.theme.Blue4
import com.cosmic_struck.stellar.ui.theme.Blue5
import com.google.gson.Gson

@Composable
fun ScanTextScreen(
    viewModel: ScanTextViewModel = hiltViewModel<ScanTextViewModel>(),
    navigateBack: () -> Unit,
    navigateToResults: (String?) -> Unit,
    modifier: Modifier = Modifier) {

    val state = viewModel.state.value
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Blue4,
                        Blue5
                    )
                )
            ),
        containerColor = Color.Transparent,
        topBar = {
            TopBarScanTextBook(navigateBack)
        }
    ) {it->

        DisposableEffect(true) {
            onDispose {
                viewModel.resetState()
            }
        }
        LaunchedEffect(state.switchToResults) {
            if (state.switchToResults) {
                val gson = Gson()
                val listJson = Uri.encode(gson.toJson(state.detections))
                navigateToResults(listJson)
            }
        }
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ){
            CameraScanText(
                imageCapture = state.imageCapture,
                modifier = Modifier
                    .fillMaxSize()
            )
            CameraScanCaption(
                modifier = Modifier
                    .align(
                        Alignment.Center
                    )
            )
            RectangleFrame(
                modifier = Modifier
                    .align(Alignment.Center)
            )
            ScanButtonScanText(
                { viewModel.captureImage(
                    context = context,
                    imageCapture = state.imageCapture,
                    onImageCaptured = { file ->
                        viewModel.uploadImage(file)
                    }
                ) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            )
        }

    }
}