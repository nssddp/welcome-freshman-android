package com.example.welcome_freshman.feature.accomplish

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
import com.example.welcome_freshman.feature.detail.DETAIL_ROUTE
import com.example.welcome_freshman.feature.detail.DetailRoute

/**
 *@date 2024/3/5 13:51
 *@author GFCoder
 */

const val ACCOMPLISH_ROUTE = "Accomplish_route"

fun NavController.navigateToAccomplish(id: String) {
    this.navigate(ACCOMPLISH_ROUTE) {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.accomplishScreen(onBackClick: () -> Unit) {
    composable(
        route = ACCOMPLISH_ROUTE,
        enterTransition = {
            fadeIn(
                animationSpec = tween(
                    500, easing = LinearEasing
                )
            ) + slideIntoContainer(
                animationSpec = tween(500, easing = EaseIn),
                towards = AnimatedContentTransitionScope.SlideDirection.Start
            )
        },
        exitTransition = {
            fadeOut(
                animationSpec = tween(
                    500, easing = LinearEasing
                )
            ) + slideOutOfContainer(
                animationSpec = tween(500, easing = EaseOut),
                towards = AnimatedContentTransitionScope.SlideDirection.End
            )
        }
    ) {
        AccomplishRoute(onBackClick = onBackClick)
    }
}