package com.example.welcome_freshman.feature.register

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
        route = REGISTER_ROUTE,
        enterTransition = {
            fadeIn(
                animationSpec = tween(
                    100, easing = LinearEasing
                )
            ) + slideIntoContainer(
                animationSpec = tween(100, easing = EaseIn),
                towards = AnimatedContentTransitionScope.SlideDirection.Start
            )
        },
        exitTransition = {
            fadeOut(
                animationSpec = tween(
                    100, easing = LinearEasing
                )
            ) + slideOutOfContainer(
                animationSpec = tween(100, easing = EaseOut),
                towards = AnimatedContentTransitionScope.SlideDirection.End
            )
        }
    ) {
        RegisterRoute()
    }
}