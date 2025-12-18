package com.cosmic_struck.stellar.common.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cosmic_struck.stellar.R
import com.cosmic_struck.stellar.common.components.BackgroundScaffold
import com.cosmic_struck.stellar.common.components.BottomAppBar
import com.cosmic_struck.stellar.homeScreen.presentation.HomeScreen
import com.cosmic_struck.stellar.stellar.models.presentation.modelScreen.ModelScreen
import com.cosmic_struck.stellar.modelScreen.presentation.modelScreen.components.ModelTopAppBar
import com.cosmic_struck.stellar.modelScreen.modelViewerFeature.presentation.ModelViewerScreen
import com.cosmic_struck.stellar.modelScreen.modelViewerFeature.presentation.components.ModelViewerTopAppBar
import com.cosmic_struck.stellar.scanTextFeature.presentation.ScanTextScreen
import com.cosmic_struck.stellar.scanTextResultFeature.presentation.ScanTextResultScreen
import com.cosmic_struck.stellar.stellar.arlab.presentation.ARLabScreen

@OptIn(ExperimentalMaterial3Api::class)
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
                        navigateToModelViewer = {it1,it2->
                            navHostController.navigate(Screens.ModelViewerScreen.route+"/$it1/$it2")
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
                    navigateToResults = {it->
                        navHostController.navigate(
                            Screens.ScanTextResultScreen.route + "/$it",
                        )
                    },
                    navigateBack = {
                        navHostController.popBackStack()
                    }
                )
            }

            composable(Screens.ScanTextResultScreen.route + "/{detections}"){
                BackgroundScaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    "Scan Result",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = {
                                    navHostController.popBackStack()
                                }) {
                                    Icon(
                                        painterResource(R.drawable.back),
                                        contentDescription = "Back",
                                        tint = Color.White
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color(0xFF1A2B4D)
                            )
                        )
                    },
                    navController = navHostController
                ) {
                    ScanTextResultScreen(
                        navigateBack = {
                            navHostController.popBackStack()
                        }
                    )
                }
            }
            composable(
                Screens.ModelViewerScreen.route+"/{name}/{id}",
                arguments = listOf(
                    navArgument("name",{type = NavType.StringType}),
                    navArgument("id",{type= NavType.StringType})
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
