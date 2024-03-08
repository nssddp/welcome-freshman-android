package com.example.welcome_freshman.mainNav

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import com.example.welcome_freshman.feature.accomplish.accomplishScreen
import com.example.welcome_freshman.feature.accomplish.navigateToAccomplish
import com.example.welcome_freshman.feature.certification.cameraScreen
import com.example.welcome_freshman.feature.certification.navigateToCamera
import com.example.welcome_freshman.feature.detail.detailScreen
import com.example.welcome_freshman.feature.detail.navigateToDetail
import com.example.welcome_freshman.feature.login.LOGIN_GRAPH
import com.example.welcome_freshman.feature.login.LoginViewModel
import com.example.welcome_freshman.feature.login.loginGraph
import com.example.welcome_freshman.feature.main.list.listScreen
import com.example.welcome_freshman.feature.main.profile.profileScreen
import com.example.welcome_freshman.feature.main.task.TASK_ROUTE
import com.example.welcome_freshman.feature.main.task.taskScreen
import com.example.welcome_freshman.feature.register.navigateToRegister
import com.example.welcome_freshman.feature.register.registerScreen
import com.example.welcome_freshman.feature.updateUserInfo.navigateToUpdateUser
import com.example.welcome_freshman.feature.updateUserInfo.updateUserScreen
import com.example.welcome_freshman.ui.WfAppState
import kotlinx.coroutines.launch

/**
 *@date 2024/1/25 16:56
 *@author GFCoder
 */

@Composable
fun WfNavHost(
    appState: WfAppState,
    startDestination: String = LOGIN_GRAPH,
    modifier: Modifier = Modifier
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = {
            fadeIn(animationSpec = tween(100))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(100))
        }
    ) {
        loginGraph(
            onRegisterClick = navController::navigateToRegister,
            onLoginClick = {
                navController.navigate(TASK_ROUTE) {
                    popUpTo(LOGIN_GRAPH) {
                        inclusive = true
                    }
                }
            },
            nestedGraphs = {
                registerScreen()
            }
        )
        taskScreen(onDetailClick = navController::navigateToDetail)
        listScreen()
        profileScreen(
            onAuthenticationClick = navController::navigateToCamera,
            onUpdateUserClick = navController::navigateToUpdateUser
        )
        detailScreen(
            onBackClick = { navController.popBackStack() },
            onAccomplishClick = navController::navigateToAccomplish
        )
        cameraScreen()
        accomplishScreen(onBackClick = { navController.popBackStack() })
        updateUserScreen(onBackClick = { navController.popBackStack() })
    }

    BackHandler(enabled = appState.drawerState.isOpen) {
        appState.scope.launch {
            appState.drawerState.close()
        }
    }
}
