package com.example.welcome_freshman.feature.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation

/**
 *@date 2024/1/25 17:17
 *@author GFCoder
 */

const val LOGIN_GRAPH = "login_graph"
const val LOGIN_ROUTE = "login_route"

fun NavController.navigateToLoginGraph(navOptions: NavOptions) = navigate(LOGIN_GRAPH, navOptions)

fun NavGraphBuilder.loginGraph(
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit
){
    navigation(
        route = LOGIN_GRAPH,
        startDestination = LOGIN_ROUTE
    ) {
        composable(route = LOGIN_ROUTE) {
            LoginRoute(onRegisterClick = onRegisterClick, onLoginClick = onLoginClick)
        }
        nestedGraphs()
    }
}