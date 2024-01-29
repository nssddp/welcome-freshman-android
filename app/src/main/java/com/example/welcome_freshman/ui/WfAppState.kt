package com.example.welcome_freshman.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.welcome_freshman.feature.main.list.LIST_ROUTE
import com.example.welcome_freshman.feature.main.list.navigateToList
import com.example.welcome_freshman.feature.main.profile.PROFILE_ROUTE
import com.example.welcome_freshman.feature.main.profile.navigateToProfile
import com.example.welcome_freshman.feature.main.task.TASK_ROUTE
import com.example.welcome_freshman.feature.main.task.navigateToTask
import com.example.welcome_freshman.mainNav.TopLevelDestination
import kotlinx.coroutines.CoroutineScope

/**
 *@date 2024/1/25 15:44
 *@author GFCoder
 */

@Composable
fun rememberWfAppState(
//    windowSizeClass: WindowSizeClass,
    scope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController()
): WfAppState {
    return remember(
//        windowSizeClass,
        scope,
        navController
    ) {
        WfAppState(
//            windowSizeClass,
            scope,
            navController
        )
    }
}


@Stable
class WfAppState(
//    val windowSizeClass: WindowSizeClass,
    val scope: CoroutineScope,
    val navController: NavHostController
) {

    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.values().asList()

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            TASK_ROUTE -> TopLevelDestination.TASK
            LIST_ROUTE -> TopLevelDestination.LIST
            PROFILE_ROUTE -> TopLevelDestination.PROFILE
            else -> null
        }

    val shouldShowBar: Boolean
        @Composable get() = currentTopLevelDestination != null

    /**
     *  底部导航跳转
     */
    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            popUpTo(TASK_ROUTE) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.TASK -> navController.navigateToTask(topLevelNavOptions)
            TopLevelDestination.LIST -> navController.navigateToList(topLevelNavOptions)
            TopLevelDestination.PROFILE -> navController.navigateToProfile(topLevelNavOptions)
        }

    }

}