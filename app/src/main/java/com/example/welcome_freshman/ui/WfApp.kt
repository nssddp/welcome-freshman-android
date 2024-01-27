package com.example.welcome_freshman.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.DensityMedium
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.example.welcome_freshman.mainNav.TopLevelDestination
import com.example.welcome_freshman.mainNav.WfNavHost
import com.example.welcome_freshman.ui.component.WfNavigationBar
import com.example.welcome_freshman.ui.component.WfNavigationBarItem
import com.example.welcome_freshman.ui.component.WfTopAppBar

/**
 *@date 2024/1/25 23:00
 *@author GFCoder
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WfApp(appState: WfAppState = rememberWfAppState()) {

    Scaffold(
        topBar = {
            val destination = appState.currentTopLevelDestination
            if (destination != null) {
                WfTopAppBar(
                    titleRes = destination.titleTextId,
                    navigationIcon = Icons.Rounded.Menu,
                    navigationIconContentDescription = "",
                    actionIcon = Icons.Rounded.Settings,
                    actionIconContentDescription = ""
                )
            }
        },
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
        WfNavHost(
            paddingValues = it,
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