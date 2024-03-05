package com.example.welcome_freshman.feature.main.task

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

/**
 *@date 2024/1/27 10:39
 *@author GFCoder
 */

const val TASK_ROUTE = "task_route"

fun NavController.navigateToTask(navOptions: NavOptions) {
    this.navigate(TASK_ROUTE, navOptions)
}

fun NavGraphBuilder.taskScreen(onDetailClick: (String) -> Unit = {}) {
    composable(
        route = TASK_ROUTE
    ) {
        TaskRoute(onDetailClick = onDetailClick)
    }
}