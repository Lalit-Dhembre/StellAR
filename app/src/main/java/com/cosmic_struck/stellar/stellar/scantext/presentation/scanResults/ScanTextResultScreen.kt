package com.cosmic_struck.stellar.scanTextResultFeature.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cosmic_struck.stellar.R
import com.cosmic_struck.stellar.common.util.Rajdhani
import com.cosmic_struck.stellar.ui.theme.Blue4
import com.cosmic_struck.stellar.ui.theme.Blue5
import com.cosmic_struck.stellar.ui.theme.DarkBlue1

@Composable
fun ScanTextResultScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier) {
    Scaffold(
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DarkBlue1,
                        Blue5,
                        DarkBlue1
                    )
                )
            ),
        containerColor = Color.Transparent,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            ){
                IconButton(
                    onClick = {
                        navigateBack()
                    },
                    modifier = Modifier
                        .size(30.dp)
                        .padding(start = 10.dp)
                        .align(Alignment.TopStart)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.back),
                        contentDescription = null,
                        tint = Color.White
                    )
                }

                Text(
                    text = "Scan Result",
                    modifier = Modifier.align(Alignment.TopCenter),
                    fontFamily = Rajdhani,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    ) {it->
        Image(
            painter = painterResource(R.drawable.starry_bg),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment
                .Center
        ){

        }
    }
}