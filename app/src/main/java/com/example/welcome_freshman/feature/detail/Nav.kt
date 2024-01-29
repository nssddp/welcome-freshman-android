package com.example.welcome_freshman.feature.detail

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
import com.example.welcome_freshman.feature.register.RegisterRoute

/**
 *@date 2024/1/28 9:31
 *@author GFCoder
 */

const val DETAIL_ROUTE = "detail_route"

fun NavController.navigateToDetail() {
    this.navigate(DETAIL_ROUTE) {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.detailScreen() {
    composable(
        route = DETAIL_ROUTE,
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
        DetailRoute()
    }
}