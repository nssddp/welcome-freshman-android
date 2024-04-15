package com.example.welcome_freshman.feature.login

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
) {
    navigation(
        route = LOGIN_GRAPH,
        startDestination = LOGIN_ROUTE
    ) {
        composable(
            route = LOGIN_ROUTE,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        500, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(500, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        500, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(500, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            }
        ) {
            LoginRoute(onRegisterClick = onRegisterClick, onLoginClick = onLoginClick)
        }
        nestedGraphs()
    }
}