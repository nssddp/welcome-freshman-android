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
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import java.nio.charset.StandardCharsets

/**
 *@date 2024/1/28 9:31
 *@author GFCoder
 */

const val TASK_ID_ARG = "taskId"
const val DETAIL_ROUTE = "detail_route"
//const val DETAIL_ROUTE_ARG = "$DETAIL_ROUTE?$TASK_ID_ARG={$TASK_ID_ARG}"

private val URL_CHARACTER_ENCODING = StandardCharsets.UTF_8.name()


fun NavController.navigateToDetail(taskId: String? = null) {
    this.navigate("$DETAIL_ROUTE/$taskId") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.detailScreen(
    onBackClick: () -> Unit,
    onAccomplishClick: (String?,String?) -> Unit,
    onQuizClick: (Int) -> Unit,
) {
    composable(
        route = "$DETAIL_ROUTE/{$TASK_ID_ARG}",
        arguments = listOf(navArgument(TASK_ID_ARG) {
            /*defaultValue = null
            nullable = true*/
            type = NavType.StringType
        }),
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
        DetailRoute(onBackClick = onBackClick, onAccomplishClick = onAccomplishClick, onQuizClick = onQuizClick)
    }
}