package com.example.welcome_freshman.mainNav

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.outlined.AssignmentTurnedIn
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Face
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import com.example.welcome_freshman.R

/**
 *@date 2024/1/27 11:00
 *@author GFCoder
 */

enum class TopLevelDestination(
    val selectedIcon : Int,
    val unselectedIcon: Int,
    val iconText: String,
    val titleTextId: Int,
) {
    TASK(
        selectedIcon =  R.drawable.task_selected,
        unselectedIcon = R.drawable.task_unselected,
        iconText = "任务",
        titleTextId = R.string.task_title,
    ),
    RANK(
        selectedIcon = R.drawable.rank_selected,
        unselectedIcon = R.drawable.rank_unselected,
        iconText = "排行榜",
        titleTextId = R.string.list_title,
    ),
    PROFILE(
        selectedIcon = R.drawable.profile_selected,
        unselectedIcon = R.drawable.profile_unselected,
        iconText = "我的",
        titleTextId = R.string.profile_title,
    ),
}


