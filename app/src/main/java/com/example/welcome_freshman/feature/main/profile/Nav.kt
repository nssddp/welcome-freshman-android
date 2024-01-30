package com.example.welcome_freshman.feature.main.profile

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

/**
 *@date 2024/1/27 10:39
 *@author GFCoder
 */

const val PROFILE_ROUTE = "profile_route"

fun NavController.navigateToProfile(navOptions: NavOptions? = null) {
    this.navigate(PROFILE_ROUTE, navOptions)
}

fun NavGraphBuilder.profileScreen(onAuthenticationClick: () -> Unit) {
    composable(
        route = PROFILE_ROUTE
    ) {
        ProfileRoute(onAuthenticationClick = onAuthenticationClick)
    }
}