package com.cosmic_struck.stellar.arlabScreen.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.cosmic_struck.stellar.common.components.BackgroundScaffold

@Composable
fun ARLabScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = "AR Lab Screen"
        )
    }
}