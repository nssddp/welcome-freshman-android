package com.example.welcome_freshman.feature.main.rank

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

/**
 *@date 2024/1/27 10:49
 *@author GFCoder
 */

const val RANK_ROUTE = "rank_route"

fun NavController.navigateToRank(navOptions: NavOptions) {
    this.navigate(RANK_ROUTE, navOptions)
}

fun NavGraphBuilder.rankScreen() {
    composable(
        route = RANK_ROUTE
    ) {
        RankRoute()
    }
}