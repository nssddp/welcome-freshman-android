package com.example.welcome_freshman.feature.main.task

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

/**
 *@date 2024/1/27 10:39
 *@author GFCoder
 */

const val TASK_ROUTE = "task_route"

const val IS_COMPLETE_TASK = "isCompleteTask"

fun NavController.navigateToTask( navOptions: NavOptions) {
    /*val route =
        if (shouldShowAD == false) TASK_ROUTE
        else "$TASK_ROUTE?$IS_COMPLETE_TASK=$shouldShowAD"*/
    this.navigate(TASK_ROUTE, navOptions)
}

fun NavGraphBuilder.taskScreen(onDetailClick: (String) -> Unit = {}) {
    composable(
        route = TASK_ROUTE,
        /*arguments = listOf(
            navArgument(IS_COMPLETE_TASK) {
                type = NavType.BoolType
                defaultValue = false
            },
        ),*/
    ) {
        TaskRoute(onDetailClick = onDetailClick)
    }
}