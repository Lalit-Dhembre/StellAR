package com.cosmic_struck.stellar.common.navigation

import android.util.Log
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.cosmic_struck.stellar.R
import com.cosmic_struck.stellar.common.components.BackgroundScaffold
import com.cosmic_struck.stellar.common.components.BottomAppBar
import com.cosmic_struck.stellar.home.presentation.HomeScreen
import com.cosmic_struck.stellar.home.presentation.components.UserTopBar
import com.cosmic_struck.stellar.stellar.home.presentation.StellarHomeScreen
import com.cosmic_struck.stellar.stellar.models.presentation.modelViewer.ModelViewerScreen
import com.cosmic_struck.stellar.stellar.models.presentation.modelViewer.components.ModelViewerTopAppBar
import com.cosmic_struck.stellar.stellar.models.presentation.modelScreen.components.ModelTopAppBar
import com.cosmic_struck.stellar.stellar.scantext.presentation.scanScreen.ScanTextScreen
import com.cosmic_struck.stellar.stellar.scantext.presentation.scanResults.ScanTextResultScreen
import com.cosmic_struck.stellar.stellar.arlab.presentation.ARLabScreen
import com.cosmic_struck.stellar.auth.presentation.AuthScreen
import com.cosmic_struck.stellar.auth.presentation.CreateAccountScreenEmailValidation
import com.cosmic_struck.stellar.auth.presentation.CreateAccountScreenPasswordValidation
import com.cosmic_struck.stellar.auth.presentation.LoginAccountScreen
import com.cosmic_struck.stellar.biology.home.BiologyHomeScreen
import com.cosmic_struck.stellar.chemistry.home.ChemistryHomeScreen
import com.cosmic_struck.stellar.history.home.HistoryHomeScreen
import com.cosmic_struck.stellar.physics.home.PhysicsHomeScreen
import com.cosmic_struck.stellar.stellar.models.presentation.modelScreen.ModelScreen
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
        NavHost(navHostController, startDestination = if(auth!= null) Screens.HomeScreen.route else Screens.AuthScreen.route) {

            composable(
                route = Screens.HomeScreen.route
            ){
                BackgroundScaffold(
                    navController = navHostController,
                    topBar = {
                        UserTopBar()
                    },
                    color = Color.White,
                    bottomBar = {}
                ) {
                    HomeScreen(
                        navController = navHostController,
                        modifier = it
                    )
                }
            }
            composable(
                route = Screens.AuthScreen.route
            ){
                BackgroundScaffold(
                    navController = navHostController
                ) {
                    AuthScreen(
                        modifier = it,
                        navController = navHostController,
                        navigateToHomeScreen = {
                            navHostController.navigate(Screens.HomeScreen.route)
                        }
                    )
                }
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

            composable(route = Screens.CreateAccountScreen.route){
                CreateAccountScreenEmailValidation(
                    navigateToPasswordValidation = {
                        navHostController.navigate(Screens.CreateAccountScreen1.route)
                    },
                    navigateback = {
                        navHostController.popBackStack()
                    }
                )
            }

            composable(route = Screens.CreateAccountScreen1.route){
                CreateAccountScreenPasswordValidation(
                    navigateback = {
                        navHostController.popBackStack()
                    },
                    navigateToHomeScreen = {
                        navHostController.navigate(Screens.HomeScreen.route){
                            popUpTo(Screens.AuthScreen.route){
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable(route = Screens.LoginScreen.route){
                LoginAccountScreen(
                    navigateback = {
                        navHostController.popBackStack()
                    },
                    navigateToHomeScreen = {
                        navHostController.navigate(Screens.HomeScreen.route){
                            popUpTo(Screens.AuthScreen.route){
                                inclusive = true
                            }
                        }
                    }
                )
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
