package com.hermanowicz.wirelesswhisper.navigation

sealed class AppScreens(
    val route: String
) {
    object MainScreen : AppScreens("MAIN_SCREEN")
}
