package com.example.welcome_freshman.feature.profile

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.welcome_freshman.feature.register.RegisterRoute

/**
 *@date 2024/1/27 10:39
 *@author GFCoder
 */

const val PROFILE_ROUTE = "profile_route"

fun NavController.navigateToProfile(navOptions: NavOptions? = null) {
    this.navigate(PROFILE_ROUTE, navOptions)
}

fun NavGraphBuilder.profileScreen() {
    composable(
        route = PROFILE_ROUTE
    ){
        ProfileRoute()
    }
}