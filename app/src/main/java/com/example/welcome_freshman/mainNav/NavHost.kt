package com.example.welcome_freshman.mainNav

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.example.welcome_freshman.feature.accomplish.accomplishScreen
import com.example.welcome_freshman.feature.certification.CAMERA_ROUTE
import com.example.welcome_freshman.feature.certification.cameraScreen
import com.example.welcome_freshman.feature.certification.navigateToCamera
import com.example.welcome_freshman.feature.detail.detailScreen
import com.example.welcome_freshman.feature.detail.navigateToDetail
import com.example.welcome_freshman.feature.login.LOGIN_GRAPH
import com.example.welcome_freshman.feature.login.loginGraph
import com.example.welcome_freshman.feature.main.profile.profileScreen
import com.example.welcome_freshman.feature.main.rank.rankScreen
import com.example.welcome_freshman.feature.main.task.TASK_ROUTE
import com.example.welcome_freshman.feature.main.task.navigateToTask
import com.example.welcome_freshman.feature.main.task.taskScreen
import com.example.welcome_freshman.feature.pointShop.navigateToShop
import com.example.welcome_freshman.feature.pointShop.shopScreen
import com.example.welcome_freshman.feature.quiz.navigateToQuiz
import com.example.welcome_freshman.feature.quiz.quizScreen
import com.example.welcome_freshman.feature.register.navigateToRegister
import com.example.welcome_freshman.feature.register.registerScreen
import com.example.welcome_freshman.feature.taskMap.navigateToTaskMap
import com.example.welcome_freshman.feature.taskMap.taskMapScreen
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
    modifier: Modifier = Modifier,
    appState: WfAppState,
    startDestination: String = LOGIN_GRAPH,
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
        rankScreen()
        profileScreen(
            onAuthenticationClick = navController::navigateToCamera,
            onUpdateUserClick = navController::navigateToUpdateUser,
            onPointShopClick = navController::navigateToShop
        )
        detailScreen(
            onBackClick = { navController.popBackStack() },
            onAccomplishClick = navController::navigateToTaskMap,
            onQuizClick = navController::navigateToQuiz
        )
        cameraScreen(onValidSuccess = {
            navController.navigate(TASK_ROUTE){
                popUpTo(CAMERA_ROUTE){
                    inclusive = true
                }
            }
            /*navController.navigateToTask(true, navOptions {
                navController.navigate(TASK_ROUTE) {
                    popUpTo(CAMERA_ROUTE) {
                        inclusive = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }*/
//            )

        })
        accomplishScreen(onBackClick = { navController.popBackStack() })
        updateUserScreen(onBackClick = { navController.popBackStack() })
        taskMapScreen(
            onBackClick = { navController.popBackStack() },
            onCheckIn = navController::navigateToCamera
        )
        quizScreen(
            onBackClick = { navController.popBackStack() },
            onCompleted = { navController.popBackStack() }
        )
        shopScreen(onBackClick = { navController.popBackStack() })


    }

    BackHandler(enabled = appState.drawerState.isOpen) {
        appState.scope.launch {
            appState.drawerState.close()
        }
    }
}
