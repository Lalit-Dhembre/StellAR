package com.cosmic_struck.stellar.home.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cosmic_struck.stellar.common.components.TabSwitcher
import com.cosmic_struck.stellar.common.util.getClassroomColor
import com.cosmic_struck.stellar.common.util.gridList
import com.cosmic_struck.stellar.home.presentation.components.ClassroomCard
import com.cosmic_struck.stellar.home.presentation.components.GridItem

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel<HomeScreenViewModel>(),
    modifier: Modifier = Modifier) {

    val state = viewModel.state.value
    Box(
        modifier = modifier
            .fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            TabSwitcher(
                onOptionSelected = {
                    viewModel.onToggle(it)
                },
                activeTextColor = Color.White,
                nonActiveTextColor = Color.Black,
                options = listOf("Modules","Classroom"),
                modifier = Modifier
                    .height(40.dp)
                    .padding(horizontal = 16.dp),
            )
            if(state.selected == Options.MODULES){
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .padding(horizontal = 20.dp),

                    ) {
                    items(gridList){
                        GridItem(
                            modifier = Modifier
                                .padding(10.dp),
                            onClick = { navController.navigate(it.navigationRoute)},
                            color = it.color,
                            title = it.title,
                            icon = it.icon
                        )
                    }
                }
            }

            else{
                LazyColumn(
                    modifier = Modifier
                        .padding(horizontal = 20.dp),

                ) {
                    item(state.joinedClassrooms) {
                        state.joinedClassrooms.forEach { it ->
                            ClassroomCard(
                                classroom = it,
                                modifier = Modifier
                                    .padding(10.dp)
                            )
                        }
                    }
                }
            }

        }


    }
}




