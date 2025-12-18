package com.cosmic_struck.stellar.stellar.models.presentation.modelScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cosmic_struck.stellar.modelScreen.presentation.modelScreen.components.ListToggler
import com.cosmic_struck.stellar.stellar.models.presentation.modelScreen.components.PlanetCard
import com.cosmic_struck.stellar.modelScreen.presentation.modelScreen.components.ScoreRow
import com.cosmic_struck.stellar.stellar.models.domain.model.Planet

@Composable
fun ModelScreen(
    navigateToModelViewer: (String, String) -> Unit,
    viewModel: ModelScreenViewModel = hiltViewModel<ModelScreenViewModel>(),
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        val state = viewModel.state.value
        val lazyListState = rememberLazyListState()
        val isScrollingDown = remember { mutableStateOf(false) }
        var previousScrollOffset = remember { 0 }

        // Detect scroll direction
        LaunchedEffect(lazyListState) {
            snapshotFlow { lazyListState.firstVisibleItemScrollOffset }
                .collect { currentOffset ->
                    isScrollingDown.value = currentOffset > previousScrollOffset
                    previousScrollOffset = currentOffset
                }
        }

        // Animated visibility for ScoreRow
        AnimatedVisibility(
            visible =  lazyListState.firstVisibleItemScrollOffset == 0,
            enter = slideInVertically(
                initialOffsetY = { -it },
            ),
            exit = slideOutVertically(
                targetOffsetY = { -it },
            )
        ) {
            ScoreRow()
        }

        Spacer(modifier = Modifier.height(10.dp))

        ListToggler(
            initialIndex = state.currentIndex,
            onOptionSelected = { viewModel.onToggleList() },
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            state = lazyListState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            items(
                items = if (state.currentList == ListType.MY_COLLECTION) viewModel.collectedList else viewModel.discoverList,
            ) { planet ->
                PlanetCard(
                    locked = if (state.currentList == ListType.MY_COLLECTION) false else true,
                    modifier = Modifier.fillMaxWidth(),
                    navigateToModelViewer = navigateToModelViewer,
                    planet = planet
                )
            }
        }
    }
}