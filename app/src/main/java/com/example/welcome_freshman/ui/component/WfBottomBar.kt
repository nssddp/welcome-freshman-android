package com.example.welcome_freshman.ui.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 *@date 2024/1/27 19:42
 *@author GFCoder
 */

@Composable
fun RowScope.WfNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    selectedIcon: @Composable () -> Unit = icon,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    alwaysShowLabel: Boolean = selected,
) {

    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = if (selected) selectedIcon else icon,
        modifier = modifier,
        enabled = enabled,
        label = label,
        alwaysShowLabel = alwaysShowLabel,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = WfNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = WfNavigationDefaults.navigationContentColor(),
            selectedTextColor = WfNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = WfNavigationDefaults.navigationContentColor(),
            indicatorColor = WfNavigationDefaults.navigationIndicatorColor(),
        ),
    )
}

@Composable
fun WfNavigationBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    NavigationBar(
        modifier = modifier,
        containerColor = Color.Transparent,
        contentColor =  Color.Transparent/*WfNavigationDefaults.navigationContentColor()*/,
        tonalElevation = 0.dp,
        content = content,
    )
}


object WfNavigationDefaults {
    @Composable
    fun navigationContentColor() = MaterialTheme.colorScheme.onSurfaceVariant

    @Composable
    fun navigationSelectedItemColor() = MaterialTheme.colorScheme.onPrimaryContainer

    @Composable
    fun navigationIndicatorColor() = MaterialTheme.colorScheme.primaryContainer
}