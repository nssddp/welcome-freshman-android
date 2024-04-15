package com.example.welcome_freshman.feature.quiz

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
import com.example.welcome_freshman.feature.accomplish.AccomplishRoute

/**
 *@date 2024/4/9 20:50
 *@author GFCoder
 */

const val QUIZ_ROUTE = "quiz_route"
const val SUB_TASK_TO_QUIZ_ID_ARG = "subTaskId"

fun NavController.navigateToQuiz(subTaskId:Int) {
    this.navigate("$QUIZ_ROUTE/$subTaskId") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.quizScreen(onBackClick: () -> Unit, onCompleted: () -> Unit) {
    composable(
        route = "$QUIZ_ROUTE/{$SUB_TASK_TO_QUIZ_ID_ARG}",
        arguments = listOf(
            navArgument(SUB_TASK_TO_QUIZ_ID_ARG){
                type = NavType.IntType
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
        QuizRoute(onBackClick = onBackClick, onCompleted = onCompleted)
    }
}