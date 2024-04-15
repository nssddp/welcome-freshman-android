package com.example.welcome_freshman.feature.pointShop

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

/**
 *@date 2024/4/14 22:15
 *@author GFCoder
 */

const val SHOP_ROUTE = "shop_route"

fun NavController.navigateToShop() {
    this.navigate(SHOP_ROUTE) {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.shopScreen(onBackClick: () -> Unit) {
    composable(
        route = SHOP_ROUTE
    ) {
        ShopRoute(onBackClick = onBackClick)
    }
}