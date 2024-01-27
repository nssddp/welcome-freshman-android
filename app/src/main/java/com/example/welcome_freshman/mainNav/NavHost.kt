package com.example.welcome_freshman.mainNav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.example.welcome_freshman.feature.list.listScreen
import com.example.welcome_freshman.feature.login.LOGIN_GRAPH
import com.example.welcome_freshman.feature.login.loginGraph
import com.example.welcome_freshman.feature.profile.profileScreen
import com.example.welcome_freshman.feature.register.navigateToRegister
import com.example.welcome_freshman.feature.register.registerScreen
import com.example.welcome_freshman.feature.task.TASK_ROUTE
import com.example.welcome_freshman.feature.task.taskScreen
import com.example.welcome_freshman.mainNav.TopLevelDestination.TASK
import com.example.welcome_freshman.ui.WfAppState

/**
 *@date 2024/1/25 16:56
 *@author GFCoder
 */

@Composable
fun WfNavHost(
    appState: WfAppState,
    startDestination: String = LOGIN_GRAPH,
    modifier: Modifier
) {

    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        loginGraph(
            onRegisterClick = navController::navigateToRegister,
            onLoginClick = { navController.navigate(TASK_ROUTE){
                popUpTo(LOGIN_GRAPH){
                    inclusive = true
                }
            } },
            nestedGraphs = {
                registerScreen()
            }
        )
        taskScreen()
        listScreen()
        profileScreen()

    }
}
