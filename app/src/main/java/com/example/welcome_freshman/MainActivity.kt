package com.example.welcome_freshman

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.baidu.location.LocationClient
import com.example.welcome_freshman.core.data.model.DarkThemeConfig
import com.example.welcome_freshman.feature.ad.AdScreen
import com.example.welcome_freshman.feature.login.LOGIN_GRAPH
import com.example.welcome_freshman.feature.main.task.TASK_ROUTE
import com.example.welcome_freshman.ui.WfApp
import com.example.welcome_freshman.ui.theme.WelcomeFreshmanTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LocationClient.setAgreePrivacy(true)

        val splashScreen = installSplashScreen()
        var uiState: MainActivityUiState by mutableStateOf(MainActivityUiState.Loading)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.onEach { uiState = it }.collect()
            }
        }

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT, Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT, Color.TRANSPARENT
            )
        )

        setContent {
            val adUrl by viewModel.adUrl.collectAsState()
            val painter = rememberAsyncImagePainter(model = adUrl)

            splashScreen.setKeepOnScreenCondition {
//                if(painter.state is AsyncImagePainter.State.Success) {
                    when (uiState) {
                        MainActivityUiState.Loading -> true
                        is MainActivityUiState.Success -> {
//                        while (){}
                            false
                        }
                    }
                /*}else
                    true*/
            }


            val darkTheme = shouldUseDarkTheme(uiState = uiState)

            WelcomeFreshmanTheme(
                darkTheme = darkTheme
            ) {
                val startDestination = when (uiState) {
                    is MainActivityUiState.Loading -> ""
                    is MainActivityUiState.Success -> {
                        Log.d(
                            "userId",
                            (uiState as MainActivityUiState.Success).userData.userId.toString()
                        )
                        if ((uiState as MainActivityUiState.Success).userData.userId == 0) {
                            LOGIN_GRAPH
                        } else {
                            TASK_ROUTE
                        }
                    }

                }

                var count by rememberSaveable {
                    mutableIntStateOf(10)
                }
                /*var showAd by remember {
                    mutableStateOf(true)
                }*/
                LaunchedEffect(key1 = Unit, block = {
                    while (count > 0) {
                        delay(1000)
                        count -= 1
                    }
                })

                if (startDestination.isNotBlank()) {
                    if (count <= 0) {
                        Log.d("startDestination", startDestination)
                        WfApp(startDestination = startDestination)
                    } else {
                        AdScreen(count = { count }, painter = { painter }, onSkipClick = { count = 0 })

                    }
                }


            }

        }
    }
}

@Composable
private fun shouldUseDarkTheme(
    uiState: MainActivityUiState,
): Boolean = when (uiState) {
    MainActivityUiState.Loading -> isSystemInDarkTheme()
    is MainActivityUiState.Success -> when (uiState.userData.darkThemeConfig) {
        DarkThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
        DarkThemeConfig.LIGHT -> false
        DarkThemeConfig.DARK -> true
    }
}
