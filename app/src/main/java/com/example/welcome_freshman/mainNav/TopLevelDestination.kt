package com.example.welcome_freshman.mainNav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.outlined.AssignmentTurnedIn
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Face
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.welcome_freshman.R

/**
 *@date 2024/1/27 11:00
 *@author GFCoder
 */

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconText: String,
    val titleTextId: Int,
) {
    TASK(
        selectedIcon = Icons.Filled.AssignmentTurnedIn,
        unselectedIcon = Icons.Outlined.AssignmentTurnedIn,
        iconText = "任务",
        titleTextId = R.string.task_title,
    ),
    RANK(
        selectedIcon = Icons.Filled.BarChart,
        unselectedIcon = Icons.Outlined.BarChart,
        iconText = "排行榜",
        titleTextId = R.string.list_title,
    ),
    PROFILE(
        selectedIcon = Icons.Filled.Face,
        unselectedIcon = Icons.Outlined.Face,
        iconText = "我的",
        titleTextId = R.string.profile_title,
    ),
}


