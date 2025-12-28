package com.cosmic_struck.stellar.common.navigation

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.cosmic_struck.stellar.R
import com.cosmic_struck.stellar.auth.presentation.screens.AuthScreen
import com.cosmic_struck.stellar.auth.presentation.viewmodel.AuthViewModel
import com.cosmic_struck.stellar.biology.home.BiologyHomeScreen
import com.cosmic_struck.stellar.chemistry.home.ChemistryHomeScreen
import com.cosmic_struck.stellar.common.components.BackgroundScaffold
import com.cosmic_struck.stellar.common.components.BottomAppBar
import com.cosmic_struck.stellar.history.home.HistoryHomeScreen
import com.cosmic_struck.stellar.home.presentation.screens.HomeScreen
import com.cosmic_struck.stellar.physics.home.PhysicsHomeScreen
import com.cosmic_struck.stellar.stellar.arlab.presentation.ARLabScreen
import com.cosmic_struck.stellar.stellar.home.presentation.StellarHomeScreen
import com.cosmic_struck.stellar.stellar.models.presentation.modelScreen.ModelScreen
import com.cosmic_struck.stellar.stellar.models.presentation.modelScreen.components.ModelTopAppBar
import com.cosmic_struck.stellar.stellar.models.presentation.modelViewer.ModelViewerScreen
import com.cosmic_struck.stellar.stellar.models.presentation.modelViewer.components.ModelViewerTopAppBar
import com.cosmic_struck.stellar.stellar.scantext.presentation.scanScreen.ScanResultsScreen
import com.cosmic_struck.stellar.stellar.scantext.presentation.scanScreen.ScanTextScreen
import com.cosmic_struck.stellar.stellar.scantext.presentation.scanScreen.ScanTextViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavGraph(
    supabase: SupabaseClient,
    navHostController: NavHostController,
    modifier: Modifier = Modifier) {

        val auth = supabase.auth.currentSessionOrNull()

        Log.d("MAINNAVGRAPH",auth.toString())
        NavHost(navHostController, startDestination = if(auth!= null) Screens.HomeScreen.route else "auth_graph") {

            composable(
                route = Screens.HomeScreen.route
            ){
                    HomeScreen(
                        navController = navHostController
                    )
            }


            composable(
                route = Screens.StellarHomeScreen.route,
                deepLinks = listOf(navDeepLink {
                    uriPattern = "stellar://home"
                })) {
                BackgroundScaffold(
                    navController = navHostController,
                    bottomBar = { BottomAppBar(navHostController) }
                ) {
                    StellarHomeScreen(
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
            navigation(startDestination = Screens.ClassroomHomeScreen.route, route = "classroom_graph"){

            }

            navigation(
                route = "scan_graph",
                startDestination = Screens.ScanTextScreen.route
            ) {

                composable(Screens.ScanTextScreen.route) {entry->
                    val parentEntry = remember(entry) {
                        navHostController.getBackStackEntry("scan_graph")
                    }
                    val scanViewModel: ScanTextViewModel = hiltViewModel<ScanTextViewModel>(parentEntry)
                    ScanTextScreen(
                        viewModel = scanViewModel,
                        navigateToResults = {
                            navHostController.navigate(Screens.ScanTextResultScreen.route){
                                popUpTo(Screens.ScanTextScreen.route){
                                    inclusive = true
                                }
                            }
                        },
                        navigateBack = {
                            navHostController.popBackStack()
                        }
                    )
                }

                composable(Screens.ScanTextResultScreen.route) {entry->

                    val parentEntry = remember(entry) {
                        navHostController.getBackStackEntry("scan_graph")
                    }
                    val scanViewModel: ScanTextViewModel = hiltViewModel<ScanTextViewModel>(parentEntry)
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
                        ScanResultsScreen(
                            viewModel = scanViewModel,
                            modifier = it
                        )
                    }
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

            navigation(
                route= "auth_graph",
                startDestination = Screens.AuthScreen.route){
                composable(
                    route = Screens.AuthScreen.route
                ){it->
                    val parentEntry = remember(it) {
                        navHostController.getBackStackEntry("auth_graph")
                    }
                    val viewModel: AuthViewModel = hiltViewModel<AuthViewModel>(parentEntry)
                    BackgroundScaffold(
                        navController = navHostController
                    ) {
                        AuthScreen(
                            viewmodel = viewModel,
                            modifier = it,
                            navController = navHostController,
                            navigateToHomeScreen = {
                                navHostController.navigate(Screens.HomeScreen.route)
                            }
                        )
                    }
                }



            }



            composable(route = Screens.PhysicsHomeScreen.route){
                PhysicsHomeScreen()
            }
            composable(route = Screens.BiologyHomeScreen.route){
                BiologyHomeScreen()
            }
            composable(route = Screens.ChemistryHomeScreen.route){
                ChemistryHomeScreen()
            }
            composable(route = Screens.HistoryHomeScreen.route){
                HistoryHomeScreen()
            }

        }
    }
