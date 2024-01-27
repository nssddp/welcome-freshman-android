package com.example.welcome_freshman.feature.list

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

/**
 *@date 2024/1/27 10:49
 *@author GFCoder
 */

const val LIST_ROUTE = "list_route"

fun NavController.navigateToList(navOptions: NavOptions) {
    this.navigate(LIST_ROUTE, navOptions)
}

fun NavGraphBuilder.listScreen() {
    composable(
        route = LIST_ROUTE
    ) {
        ListRoute()
    }
}