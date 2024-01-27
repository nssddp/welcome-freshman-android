package com.example.welcome_freshman.feature.register

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

/**
 *@date 2024/1/25 17:46
 *@author GFCoder
 */

const val REGISTER_ROUTE = "register_route"

fun NavController.navigateToRegister() {
    this.navigate(REGISTER_ROUTE) {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.registerScreen() {
    composable(
        route = REGISTER_ROUTE
    ) {
        RegisterRoute()
    }
}