package com.example.welcome_freshman.feature.certification

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

/**
 *@date 2024/1/29 18:06
 *@author GFCoder
 */
const val SUB_TASK_ID_ARG = "subTaskId"

const val CAMERA_ID_ARG = "cameraId"

const val CAMERA_ROUTE = "camera_route"

fun NavController.navigateToCamera(cameraId: String, subTaskId: String? = "0000") {
    this.navigate("$CAMERA_ROUTE/$cameraId?$SUB_TASK_ID_ARG=$subTaskId") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.cameraScreen(onValidSuccess: () -> Unit) {
    composable(
        route = "$CAMERA_ROUTE/{$CAMERA_ID_ARG}?$SUB_TASK_ID_ARG={$SUB_TASK_ID_ARG}",
        arguments = listOf(
            navArgument(CAMERA_ID_ARG) {
                type = NavType.StringType
            },
            navArgument(SUB_TASK_ID_ARG) {
                type = NavType.StringType
//                nullable = true
                defaultValue = "0000"
            },
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
        CameraRoute(onValidSuccess = onValidSuccess)
    }
}