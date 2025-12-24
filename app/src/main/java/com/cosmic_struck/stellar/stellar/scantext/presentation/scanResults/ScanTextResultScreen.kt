package com.cosmic_struck.stellar.stellar.scantext.presentation.scanResults

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.cosmic_struck.stellar.common.util.Rajdhani
import com.cosmic_struck.stellar.stellar.scantext.presentation.scanResults.components.PlanetInfoCard

@Composable
fun ScanTextResultScreen(
    viewmodel: ScanTextResultsViewModel = hiltViewModel<ScanTextResultsViewModel>(),
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier) {

    val state = viewmodel.state.value

    if(state.planets.isEmpty()){
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            Text(
                text = "NO RESULTS FOUND",
                fontFamily = Rajdhani,
                color = Color.White
            )
        }
    }
    else{
        LazyColumn() {
            items(state.planets){
                PlanetInfoCard(
                    planet = it
                )
            }
        }
    }
}