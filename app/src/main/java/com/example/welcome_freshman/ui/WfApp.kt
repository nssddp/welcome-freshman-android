package com.example.welcome_freshman.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.example.welcome_freshman.mainNav.TopLevelDestination
import com.example.welcome_freshman.mainNav.WfNavHost
import com.example.welcome_freshman.mainNav.WfNavigationBar
import com.example.welcome_freshman.mainNav.WfNavigationBarItem

/**
 *@date 2024/1/25 23:00
 *@author GFCoder
 */

@Composable
fun WfApp(appState: WfAppState = rememberWfAppState()) {

    Scaffold(
        bottomBar = {
            if (appState.shouldShowBottomBar) {
                WfBottomBar(
                    destinations = appState.topLevelDestinations,
                    onNavigateToDestination = appState::navigateToTopLevelDestination,
                    currentDestination = appState.currentDestination
                )
            }
        }
    ) {
        WfNavHost(
            appState = appState,
            modifier = Modifier
                .padding(it)
        )
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