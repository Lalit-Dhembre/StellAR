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
}
