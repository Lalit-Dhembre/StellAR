package com.cosmic_struck.stellar.scanTextFeature.presentation

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cosmic_struck.stellar.R
import com.cosmic_struck.stellar.common.util.Rajdhani
import com.cosmic_struck.stellar.common.util.ScanTextBookCaptions
import com.cosmic_struck.stellar.scanTextFeature.presentation.components.CameraContentScanText
import com.cosmic_struck.stellar.scanTextFeature.presentation.components.CameraScanCaption
import com.cosmic_struck.stellar.scanTextFeature.presentation.components.CameraScanText
import com.cosmic_struck.stellar.scanTextFeature.presentation.components.RectangleFrame
import com.cosmic_struck.stellar.scanTextFeature.presentation.components.ScanButtonScanText
import com.cosmic_struck.stellar.scanTextFeature.presentation.components.TopBarScanTextBook
import com.cosmic_struck.stellar.ui.theme.Blue4
import com.cosmic_struck.stellar.ui.theme.Blue5
import com.cosmic_struck.stellar.ui.theme.ButtonPrimary
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