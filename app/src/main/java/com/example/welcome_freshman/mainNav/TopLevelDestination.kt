package com.example.welcome_freshman.mainNav

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Task
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 *@date 2024/1/27 11:00
 *@author GFCoder
 */

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconText: String,
    val titleText: String,
) {
    TASK(
        selectedIcon = Icons.Filled.Task,
        unselectedIcon = Icons.Outlined.Task,
        iconText = "任务",
        titleText = "任务",
    ),
    LIST(
        selectedIcon = Icons.Filled.List,
        unselectedIcon = Icons.Outlined.List,
        iconText = "排行榜",
        titleText = "排行榜",
    ),
    PROFILE(
        selectedIcon = Icons.Filled.Face,
        unselectedIcon = Icons.Outlined.Face,
        iconText = "我的",
        titleText = "我的",
    ),
}


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
        contentColor = WfNavigationDefaults.navigationContentColor(),
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