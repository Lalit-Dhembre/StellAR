package com.cosmic_struck.stellar.stellar.models.presentation.modelScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cosmic_struck.stellar.common.util.ImgUrl
import com.cosmic_struck.stellar.common.util.ImgUrl2
import com.cosmic_struck.stellar.common.util.SampleDesc
import com.cosmic_struck.stellar.common.util.SampleDesc2
import com.cosmic_struck.stellar.modelScreen.presentation.modelScreen.components.ListToggler
import com.cosmic_struck.stellar.modelScreen.presentation.modelScreen.components.PlanetCard
import com.cosmic_struck.stellar.modelScreen.presentation.modelScreen.components.ScoreRow

@Composable
fun ModelScreen(
    navigateToModelViewer: (String) -> Unit,
    viewModel: ModelScreenViewModel = hiltViewModel<ModelScreenViewModel>(),
    modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        val state = viewModel.state
        ScoreRow()
        Spacer(modifier = Modifier.height(10.dp))
        ListToggler(
            initialIndex = state.value.currentIndex,
            onOptionSelected = { viewModel.onToggleList() },
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(
                count = 10
            ){
                if(state.value.currentIndex == 0){
                    PlanetCard(
                        desc = SampleDesc,
                        planet = "Jupiter",
                        image = ImgUrl,
                        navigateToModelViewer = { navigateToModelViewer("Jupiter") }
                    )
                }
                else{
                    PlanetCard(
                        desc = SampleDesc2,
                        planet = "Saturn",
                        image = ImgUrl2,
                        navigateToModelViewer = { navigateToModelViewer("Saturn") }
                    )
                }
            }
        }
    }
}