package com.example.welcome_freshman.ui

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.welcome_freshman.feature.main.profile.PROFILE_ROUTE
import com.example.welcome_freshman.feature.main.profile.navigateToProfile
import com.example.welcome_freshman.feature.main.rank.RANK_ROUTE
import com.example.welcome_freshman.feature.main.rank.navigateToRank
import com.example.welcome_freshman.feature.main.task.TASK_ROUTE
import com.example.welcome_freshman.feature.main.task.navigateToTask
import com.example.welcome_freshman.mainNav.TopLevelDestination
import kotlinx.coroutines.CoroutineScope

/**
 *@date 2024/1/25 15:44
 *@author GFCoder
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberWfAppState(
//    windowSizeClass: WindowSizeClass,
    scope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
): WfAppState {
    return remember(
//        windowSizeClass,
        scope,
        navController,
        drawerState
    ) {
        WfAppState(
//            windowSizeClass,
            scope,
            navController,
            drawerState,
        )
    }
}


@Stable
class WfAppState @OptIn(ExperimentalMaterial3Api::class) constructor(
//    val windowSizeClass: WindowSizeClass,
    val scope: CoroutineScope,
    val navController: NavHostController,
    val drawerState: DrawerState,
) {


    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.values().asList()

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            TASK_ROUTE -> TopLevelDestination.TASK
            RANK_ROUTE -> TopLevelDestination.RANK
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
            TopLevelDestination.RANK -> navController.navigateToRank(topLevelNavOptions)
            TopLevelDestination.PROFILE -> navController.navigateToProfile(topLevelNavOptions)
        }

    }
//    fun navigateToProfile() = navController.navigateToProfile()
}