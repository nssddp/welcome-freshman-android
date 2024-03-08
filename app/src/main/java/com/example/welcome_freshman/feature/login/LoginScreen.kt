package com.example.welcome_freshman.feature.login

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout.RESIZE_MODE_ZOOM
import androidx.media3.ui.PlayerView
import com.example.welcome_freshman.R
import com.example.welcome_freshman.ui.theme.WelcomeFreshmanTheme
import kotlinx.coroutines.launch

/**
 *@date 2024/1/25 17:26
 *@author GFCoder
 */

@Composable
fun LoginRoute(
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {

    // 隐藏状态需用
    /*val window = (LocalContext.current as Activity).window
    val windowInsetsController by remember {
        mutableStateOf(WindowCompat.getInsetsController(window, window.decorView))
    }

    DisposableEffect(Unit) {
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        WindowCompat.setDecorFitsSystemWindows(window, false)

        onDispose {
            windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
            WindowCompat.setDecorFitsSystemWindows(window, true)

        }
    }*/
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LoginScreen(
        onRegisterClick = onRegisterClick,
        onLoginClick = { stuId, pwd ->
            scope.launch {
                if (stuId.isNotBlank() && pwd.isNotBlank()) {
                    if (viewModel.doLogin(stuId.toInt(), pwd)) {
                        onLoginClick()
                    } else {
                        context.doLogin("账号或密码输入错误，请重试!")
                    }
                } else {
                    context.doLogin("账号或密码为空!")
                }
            }
        },
    )
}

@SuppressLint("DiscouragedApi")
private fun Context.getVideoUri(): Uri {
    val rawId = resources.getIdentifier("clouds", "raw", packageName)
    val videoUri = "android.resource://${packageName}/$rawId"
    return Uri.parse(videoUri)
}

private fun Context.buildExoPlayer() =
    ExoPlayer.Builder(this).build().apply {
        setMediaItem(MediaItem.fromUri(getVideoUri()))
        repeatMode = Player.REPEAT_MODE_ALL
        playWhenReady = true
        prepare()
    }

@androidx.annotation.OptIn(UnstableApi::class)
private fun Context.buildPlayerView(exoPlayer: ExoPlayer) =
    PlayerView(this).apply {
        player = exoPlayer
        layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        useController = false
        resizeMode = RESIZE_MODE_ZOOM
    }

private fun Context.doLogin(text: String) {
    Toast.makeText(
        this,
        text,
        Toast.LENGTH_SHORT
    ).show()
}

@Composable
fun LoginScreen(onRegisterClick: () -> Unit, onLoginClick: (stuId: String, pwd: String) -> Unit) {
    val passwordFocusRequester = FocusRequester()
    val focusManager = LocalFocusManager.current

    var stuId by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }

    /*val context = LocalContext.current
    val player = remember {
        context.buildExoPlayer()
    }
    AndroidView(
        factory = { it.buildPlayerView(player) },
        modifier = Modifier.fillMaxSize()
    )
    DisposableEffect(Unit) {
        onDispose {
            player.release()
        }
    }*/

    Column(
        Modifier
            .padding(24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.Bottom),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            Modifier.size(80.dp),
            tint = Color.White
        )
        TextInput(
            InputType.StuId,
            keyboardActions = KeyboardActions(onNext = { passwordFocusRequester.requestFocus() }),
            valueChange = { stuId = it },
            showValue = { stuId }
        )
        TextInput(
            InputType.Password,
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
                onLoginClick(stuId, password)
            }),
            focusRequest = passwordFocusRequester,
            valueChange = { password = it },
            showValue = { password }
        )
        Button(
            onClick = { onLoginClick(stuId, password) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "登 录", Modifier.padding(vertical = 8.dp))
        }
        Divider(
            color = Color.White.copy(alpha = 0.3f),
            thickness = 1.dp,
            modifier = Modifier.padding(top = 48.dp)
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "还没有账户?")
            TextButton(onClick = onRegisterClick) {
                Text(text = "注 册")
            }
        }
    }
}

sealed class InputType(
    val label: String,
    val icon: ImageVector,
    val keyboardOptions: KeyboardOptions,
    val visualTransformation: VisualTransformation
) {
    object StuId : InputType(
        label = "学号",
        icon = Icons.Default.Person,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        visualTransformation = VisualTransformation.None
    )

    object Password : InputType(
        label = "密码",
        icon = Icons.Default.Lock,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        visualTransformation = PasswordVisualTransformation()
    )
}

@Composable
fun TextInput(
    inputType: InputType,
    focusRequest: FocusRequester? = null,
    keyboardActions: KeyboardActions,
    valueChange: (String) -> Unit,
    showValue: () -> String,
) {

    OutlinedTextField(
        value = showValue(),
        onValueChange = { valueChange(it) },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester = focusRequest ?: FocusRequester()),
        leadingIcon = { Icon(imageVector = inputType.icon, null) },
        label = { Text(text = inputType.label) },
//        shape = MaterialTheme.shapes.small,
        singleLine = true,
        keyboardOptions = inputType.keyboardOptions,
        visualTransformation = inputType.visualTransformation,
        keyboardActions = keyboardActions
    )

}


@Preview(showBackground = true)
@Composable
fun loginScreenPreview() {
    WelcomeFreshmanTheme {

    }

}
