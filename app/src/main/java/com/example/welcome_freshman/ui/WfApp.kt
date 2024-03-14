package com.example.welcome_freshman.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.navOptions
import com.example.welcome_freshman.feature.certification.navigateToCamera
import com.example.welcome_freshman.feature.login.LoginViewModel
import com.example.welcome_freshman.feature.login.navigateToLoginGraph
import com.example.welcome_freshman.feature.main.task.TASK_ROUTE
import com.example.welcome_freshman.mainNav.TopLevelDestination
import com.example.welcome_freshman.mainNav.WfNavHost
import com.example.welcome_freshman.ui.component.WfBackground
import com.example.welcome_freshman.ui.component.WfNavigationBar
import com.example.welcome_freshman.ui.component.WfNavigationBarItem
import com.example.welcome_freshman.ui.component.WfNavigationDrawer
import com.example.welcome_freshman.ui.component.WfTopAppBar
import kotlinx.coroutines.launch

/**
 *@date 2024/1/25 23:00
 *@author GFCoder
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WfApp(
    appState: WfAppState = rememberWfAppState(),
    startDestination: String,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    WfBackground {
        val destination = appState.currentTopLevelDestination

        WfNavigationDrawer(
            drawerState = appState.drawerState,
            gesturesEnabled = { destination != null },
            onLogoutClick = {
                appState.scope.launch {
                    appState.drawerState.close()
                    appState.navController.navigateToLoginGraph(navOptions {
                        popUpTo(TASK_ROUTE) {
                            inclusive = true
                        }
                    })
                    loginViewModel.logout()
                }
            },
            onCheckClick = {
                appState.scope.launch {
                    appState.drawerState.close()
                    appState.navController.navigateToCamera()
                }
            }
        ) {
            val scrollBehavior =
                TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                containerColor = Color.Transparent,
//            contentColor = MaterialTheme.colorScheme.onBackground,
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                bottomBar = {
                    if (appState.shouldShowBar) {
                        WfBottomBar(
                            destinations = appState.topLevelDestinations,
                            onNavigateToDestination = appState::navigateToTopLevelDestination,
                            currentDestination = appState.currentDestination
                        )
                    }
                }
            ) {

                Row(
                    Modifier
                        .fillMaxSize()
                        .padding(it)
                        .consumeWindowInsets(it)
                        .windowInsetsPadding(
                            WindowInsets.safeDrawing.only(
                                WindowInsetsSides.Horizontal,
                            ),
                        ),
                ) {
                    Box() {
                        Column(Modifier.fillMaxSize()) {

                            if (destination != null) {
                                WfTopAppBar(
                                    titleRes = destination.titleTextId,
                                    navigationIcon = Icons.Rounded.Menu,
                                    navigationIconContentDescription = "",
                                    actionIcon = Icons.Rounded.Settings,
                                    actionIconContentDescription = "",
                                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                        containerColor = Color.Transparent,
                                    ),
                                    scrollBehavior = scrollBehavior,
                                    onNavigationClick = {
                                        appState.scope.launch {
                                            appState.drawerState.apply {
                                                if (isClosed) open() else close()
                                            }
                                        }
                                    }
                                )

                            }

                            WfNavHost(
                                appState = appState,
                                startDestination = startDestination
                            )

                        }


                    }

                }

            }
        }


    }

}


@Composable
private fun WfBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
) {
    WfNavigationBar(
        modifier = modifier,
    ) {
        destinations.forEach { destination ->
            val selected = currentDestination?.hierarchy?.any {
                it.route?.contains(destination.name, true) ?: false
            } ?: false
            WfNavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    Icon(
                        imageVector = destination.unselectedIcon,
                        contentDescription = null
                    )
                },
                selectedIcon = {
                    Icon(imageVector = destination.selectedIcon, contentDescription = null)
                },
                label = { Text(text = destination.iconText) },
                modifier = Modifier,
            )

        }
    }
}