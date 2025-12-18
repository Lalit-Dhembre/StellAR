package com.cosmic_struck.stellar.stellar.scantext.presentation.scanScreen

import androidx.camera.core.ImageCapture

data class ScanImageScreenState(
    val isLoading: Boolean = false,
    val isError: String = "",
    val imageCapture: ImageCapture = ImageCapture.Builder().setCaptureMode(
        ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY
    ).build(),
    val detections: List<String> = emptyList(),
    val count: Int = 0,
    val switchToResults : Boolean = false
)
