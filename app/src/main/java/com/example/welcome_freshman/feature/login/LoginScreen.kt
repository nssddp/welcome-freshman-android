package com.example.welcome_freshman.feature.login

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout.RESIZE_MODE_ZOOM
import androidx.media3.ui.PlayerView
import com.example.welcome_freshman.R
import com.example.welcome_freshman.ui.component.LoadingDialog
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
    val uiState by viewModel.uiState.collectAsState()
    if (uiState == LoginUiState.Loading) LoadingDialog()

    LoginScreenTest(
        onLoginClick = { stuId, pwd ->
            scope.launch {
                if (stuId.isNotBlank() && pwd.isNotBlank()) {
                    val loginResponse = viewModel.doLogin(stuId.toInt(), pwd)

                    if (loginResponse.first) {
                        onLoginClick()
                    } else {
                        context.doLogin(loginResponse.second)
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
            painter = painterResource(id = R.drawable.logo_yuzu),
            contentDescription = null,
            Modifier.size(80.dp),
            tint = Color.White
        )
        TextInput(
            InputType.StuId,
            keyboardActions = KeyboardActions(onNext = { passwordFocusRequester.requestFocus() }),
            valueChange = { if (it.isDigitsOnly()) stuId = it },
            showValue = { stuId }
        )
        TextInput(
            InputType.Password,
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
                onLoginClick(stuId, password)
            }),
            focusRequest = passwordFocusRequester,
            valueChange = { if (!it.contains(' ')) password = it },
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
    val icon: Int,
    val keyboardOptions: KeyboardOptions,
    val visualTransformation: VisualTransformation
) {
    object StuId : InputType(
        label = "学号",
        icon = R.drawable.login_stu_icon,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Number
        ),
        visualTransformation = VisualTransformation.None
    )

    object Password : InputType(
        label = "密码",
        icon = R.drawable.login_password_icon,
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
    isPwdVisible: Boolean = false,
    onIsVisibleChanged: (Boolean) -> Unit = {}
) {

    TextField(
        value = showValue(),
        onValueChange = {
            valueChange(it)
        },
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .fillMaxWidth()
            .focusRequester(focusRequester = focusRequest ?: FocusRequester())
            .background(Color.White),
        leadingIcon = { Icon(painter = painterResource(id = inputType.icon), null) },
        trailingIcon = {
            if (inputType == InputType.Password) {
                val eyeIcon = if (isPwdVisible) Icons.Default.Visibility
                else Icons.Default.VisibilityOff

                IconButton(onClick = { onIsVisibleChanged(!isPwdVisible) }) {
                    Icon(imageVector = eyeIcon, null)
                }
            }

        },
        label = { Text(text = inputType.label) },
        shape = MaterialTheme.shapes.medium,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
        ),
        singleLine = true,
        keyboardOptions = inputType.keyboardOptions,
        visualTransformation = if (isPwdVisible) VisualTransformation.None else inputType.visualTransformation,
        keyboardActions = keyboardActions
    )

}

@Composable
fun LoginScreenTest(onLoginClick: (stuId: String, pwd: String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.login_background),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            OnLoginCard(
                modifier = Modifier,
                onLoginClick = onLoginClick,
            )

           /* HorizontalDivider(
                color = Color.Black,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
            )
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {

                IconButton(onClick = { *//*TODO*//* }, modifier = Modifier.size(50.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.login_wechat),
                        contentDescription = null,
                    )
                }
                Spacer(modifier = Modifier.width(26.dp))
                IconButton(onClick = { *//*TODO*//* }, modifier = Modifier.size(50.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.login_huawei),
                        contentDescription = null,
                    )
                }

            }*/


        }


    }

}

@Composable
fun OnLoginCard(modifier: Modifier = Modifier, onLoginClick: (stuId: String, pwd: String) -> Unit) {

    Card(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .fillMaxWidth()
            .height(360.dp)
            .clip(RoundedCornerShape(10.dp)),
        colors = CardDefaults.cardColors(Color.White.copy(0.5f))
    ) {
        val passwordFocusRequester = FocusRequester()
        val focusManager = LocalFocusManager.current

        var stuId by rememberSaveable {
            mutableStateOf("")
        }
        var password by rememberSaveable {
            mutableStateOf("")
        }

        var isPwdVisible by rememberSaveable {
            mutableStateOf(false)
        }

        Column(
            Modifier
                .padding(horizontal = 24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(
                16.dp,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.new_logo),
                contentDescription = null,
                Modifier
                    .padding(bottom = 10.dp)
                    .size(80.dp),
                colorFilter = ColorFilter.tint(Color(0xFF0681B9))
            )
            TextInput(
                InputType.StuId,
                keyboardActions = KeyboardActions(onNext = { passwordFocusRequester.requestFocus() }),
                valueChange = { if (it.isDigitsOnly()) stuId = it },
                showValue = { stuId },
            )
            TextInput(
                InputType.Password,
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    onLoginClick(stuId, password)
                }),
                focusRequest = passwordFocusRequester,
                valueChange = { if (!it.contains(' ')) password = it },
                showValue = { password },
                isPwdVisible = isPwdVisible,
                onIsVisibleChanged = { isPwdVisible = it }
            )
            /*Button(
                onClick = { onLoginClick(stuId, password) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "登 录", Modifier.padding(vertical = 8.dp))
            }*/
            Image(painter = painterResource(id = R.drawable.login_but), contentDescription = null,
                modifier = Modifier.size(70.dp).graphicsLayer { rotationZ = 180f }.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null){
                    onLoginClick(stuId, password)
                },
                colorFilter = ColorFilter.tint(Color(0xFF0681B9))
            )

        }
    }


}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    WelcomeFreshmanTheme {
        LoginScreenTest(onLoginClick = { str1, str2 ->
            println("$str1,$str2")
        })
    }

}
