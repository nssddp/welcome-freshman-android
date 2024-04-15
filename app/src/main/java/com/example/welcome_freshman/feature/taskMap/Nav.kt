package com.example.welcome_freshman.feature.taskMap

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
import com.example.welcome_freshman.feature.certification.CAMERA_ID_ARG

/**
 *@date 2024/4/4 23:35
 *@author GFCoder
 */

const val TASK_MAP_ROUTE = "task_map_route"
const val SUB_TASK_ID_TO_MAP_ARG = "subTaskIdMapArg"

fun NavController.navigateToTaskMap(cameraId: String? = "0", subTaskIdMapArg: String? = "0000") {
    val route = if (subTaskIdMapArg != "0000" && cameraId != "0")
        "$TASK_MAP_ROUTE?$CAMERA_ID_ARG=$cameraId&$SUB_TASK_ID_TO_MAP_ARG=$subTaskIdMapArg"
    else
        TASK_MAP_ROUTE
    this.navigate(route) {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.taskMapScreen(onBackClick: () -> Unit, onCheckIn: (String, String) -> Unit) {
    composable(
        route = "$TASK_MAP_ROUTE?$CAMERA_ID_ARG={$CAMERA_ID_ARG}&$SUB_TASK_ID_TO_MAP_ARG={$SUB_TASK_ID_TO_MAP_ARG}",
        arguments = listOf(
            navArgument(CAMERA_ID_ARG) {
                type = NavType.StringType
                defaultValue = "0"
            },
            navArgument(SUB_TASK_ID_TO_MAP_ARG) {
                type = NavType.StringType
                defaultValue = "0000"
            }
        ),
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
        TaskMapRoute(onBackClick = onBackClick, onCheckIn = onCheckIn)
    }
}