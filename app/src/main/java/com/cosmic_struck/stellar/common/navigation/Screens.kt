package com.cosmic_struck.stellar.common.navigation

sealed class Screens(val route: String) {
    object HomeScreen: Screens(route = "HomeScreen")
     object StellarHomeScreen : Screens(route = "StellarHomeScreen")
     object LoginScreen : Screens(route = "LoginScreen")
     object ModelScreen : Screens(route = "ModelScreen")
     object ARLabScreen : Screens(route = "ARLabScreen")
     object ScanTextScreen : Screens(route = "ScanTextScreen")
    object ScanTextResultScreen : Screens(route = "ScanTextResultScreen")

    object ModelViewerScreen : Screens(route = "ModelViewerScreen")
    object AuthScreen: Screens(route = "AuthScreen")

    object CreateAccountScreen : Screens(route = "CreateAccountEmail")
    object CreateAccountScreen1 : Screens(route = "CreateAccountPassword")

    object PhysicsHomeScreen: Screens(route = "PhysicsHomeScreen")
    object BiologyHomeScreen: Screens(route = "BiologyHomeScreen")
    object ChemistryHomeScreen: Screens(route = "ChemistryHomeScreen")
    object HistoryHomeScreen: Screens(route = "HistoryHomeScreen")

    object ClassroomHomeScreen: Screens(route = "ClassroomHomeScreen")
    object ClassroomModelScreen: Screens(route = "ClassroomModelScreen")
}
