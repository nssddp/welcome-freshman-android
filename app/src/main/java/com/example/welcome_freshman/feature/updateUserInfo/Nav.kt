package com.example.welcome_freshman.feature.updateUserInfo

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

/**
 *@date 2024/3/6 19:46
 *@author GFCoder
 */
const val UPDATE_USER_ROUTE = "Update_user_route"

fun NavController.navigateToUpdateUser(id: Int) {
 this.navigate(UPDATE_USER_ROUTE) {
  launchSingleTop = true
 }
}

fun NavGraphBuilder.updateUserScreen(onBackClick: () -> Unit) {
 composable(
  route = UPDATE_USER_ROUTE,
  enterTransition = {
   fadeIn(
    animationSpec = tween(
     100, easing = LinearEasing
    )
   ) + slideIntoContainer(
    animationSpec = tween(100, easing = EaseIn),
    towards = AnimatedContentTransitionScope.SlideDirection.Start
   )
  },
  exitTransition = {
   fadeOut(
    animationSpec = tween(
     100, easing = LinearEasing
    )
   ) + slideOutOfContainer(
    animationSpec = tween(100, easing = EaseOut),
    towards = AnimatedContentTransitionScope.SlideDirection.End
   )
  }
 ) {
  UpdateUserRoute(onBackClick = onBackClick)
 }
}