package com.cosmic_struck.stellar.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cosmic_struck.stellar.arlabScreen.presentation.ARLabScreen
import com.cosmic_struck.stellar.common.components.BackgroundScaffold
import com.cosmic_struck.stellar.common.components.BottomAppBar
import com.cosmic_struck.stellar.homeScreen.presentation.HomeScreen
import com.cosmic_struck.stellar.stellar.models.presentation.modelScreen.ModelScreen
import com.cosmic_struck.stellar.modelScreen.presentation.modelScreen.components.ModelTopAppBar
import com.cosmic_struck.stellar.modelScreen.modelViewerFeature.presentation.ModelViewerScreen
import com.cosmic_struck.stellar.modelScreen.modelViewerFeature.presentation.components.ModelViewerTopAppBar
import com.cosmic_struck.stellar.scanTextFeature.presentation.ScanTextScreen
import com.cosmic_struck.stellar.scanTextResultFeature.presentation.ScanTextResultScreen

@Composable
fun MainNavGraph(
    navHostController: NavHostController,
    modifier: Modifier = Modifier) {


        NavHost(navHostController, startDestination = Screens.HomeScreen.route) {

            composable(route = Screens.HomeScreen.route) {
                BackgroundScaffold(
                    navController = navHostController,
                    bottomBar = { BottomAppBar(navHostController) }
                ) {
                    HomeScreen(
                        navigateToScanText = {
                            navHostController.navigate(Screens.ScanTextScreen.route)
                        }
                    )
                }
            }
            composable(route = Screens.ModelScreen.route) {
                BackgroundScaffold(
                    navController = navHostController,
                    topBar = { ModelTopAppBar() },
                    bottomBar = { BottomAppBar(navHostController) }
                ) {
                    ModelScreen(
                        navigateToModelViewer = {it1->
                            navHostController.navigate(Screens.ModelViewerScreen.route+"/$it1")
                        },
                        modifier = it)
                }
            }
            composable(route = Screens.ARLabScreen.route) {
                BackgroundScaffold(
                    navController = navHostController,
                    bottomBar = {BottomAppBar(navHostController)}
                ) {
                    ARLabScreen()
                }
            }
            composable(Screens.ScanTextScreen.route){
                ScanTextScreen(
                    scanResult = {
                        navHostController.navigate(Screens.ScanTextResultScreen.route)
                    },
                    navigateBack = {
                        navHostController.popBackStack()
                    }
                )
            }

            composable(Screens.ScanTextResultScreen.route){
                ScanTextResultScreen(
                    navigateBack = {
                        navHostController.popBackStack()
                    }
                )
            }
            composable(
                Screens.ModelViewerScreen.route+"/{name}",
                arguments = listOf(
                    navArgument("name",{type = NavType.StringType}),
                )){backStack->
                BackgroundScaffold(
                    topBar = {
                        ModelViewerTopAppBar(
                            name = backStack.arguments?.getString("name") ?: "Model",
                            onNavigateBack = {
                                navHostController.popBackStack()
                            }
                        )
                    },
                    navController = navHostController) {
                    ModelViewerScreen()
                }
            }
        }
    }
