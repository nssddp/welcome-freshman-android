package com.example.welcome_freshman.feature.settings

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.welcome_freshman.R
import com.example.welcome_freshman.core.data.model.DarkThemeConfig
import com.example.welcome_freshman.ui.theme.BlueSky
import com.example.welcome_freshman.ui.theme.BorderColor
import com.example.welcome_freshman.ui.theme.NightSky
import com.example.welcome_freshman.ui.theme.WelcomeFreshmanTheme
import kotlinx.coroutines.launch

/**
 *@date 2024/3/27 21:54
 *@author GFCoder
 */

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val settingsUiState by viewModel.settingsUiState.collectAsState()
    SettingsDialog(
        onDismiss = onDismiss,
        settingsUiState = settingsUiState,
        onChangeDarkThemeConfig = viewModel::updateDarkThemeConfig,
    )
}

@Composable
fun SettingsDialog(
    settingsUiState: SettingsUiState,
//    supportDynamicColor: Boolean = supportsDynamicTheming(),
    onDismiss: () -> Unit,
//    onChangeDynamicColorPreference: (useDynamicColor: Boolean) -> Unit,
    onChangeDarkThemeConfig: (darkThemeConfig: DarkThemeConfig) -> Unit,
) {
    val config = LocalConfiguration.current

    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.widthIn(max = config.screenWidthDp.dp - 80.dp),
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = "设置",
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            HorizontalDivider()
            LazyColumn {
                item {
                    when (settingsUiState) {
                        SettingsUiState.Loading -> {
                            Text(
                                text = "加载中",
                                modifier = Modifier.padding(vertical = 16.dp)
                            )
                        }

                        is SettingsUiState.Success -> {
                            SettingPanel(
                                settings = settingsUiState.settings,
                                onChangeDarkThemeConfig = onChangeDarkThemeConfig
                            )
                        }

                    }
                }

                item { HorizontalDivider() }
            }
        },
        confirmButton = {
            Text(
                text = "确定",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { onDismiss() },
            )
        }
    )
}

@Composable
fun LazyItemScope.SettingPanel(
    settings: UserEditableSettings,
    onChangeDarkThemeConfig: (darkThemeConfig: DarkThemeConfig) -> Unit,
) {

    SettingsDialogSectionTitle(text = "夜间模式设置")
    SettingsDialogThemeChooserRow(
        text = "夜间模式跟随系统",
    ) {
        Spacer(Modifier.width(8.dp))
        Switch(
            checked = settings.darkThemeConfig == DarkThemeConfig.FOLLOW_SYSTEM,
            onCheckedChange = {
                if (it) onChangeDarkThemeConfig(DarkThemeConfig.FOLLOW_SYSTEM) else
                    onChangeDarkThemeConfig(DarkThemeConfig.LIGHT)
            }
        )
    }
    SettingsDialogThemeChooserRow(
        text = "夜间模式切换",
    ) {
        Spacer(Modifier.width(8.dp))
        DarkModeSwitch(
            checked = settings.darkThemeConfig == DarkThemeConfig.DARK ||
                    (isSystemInDarkTheme() && settings.darkThemeConfig == DarkThemeConfig.FOLLOW_SYSTEM)
        ) {
            if (it) onChangeDarkThemeConfig(DarkThemeConfig.DARK)
            else onChangeDarkThemeConfig(DarkThemeConfig.LIGHT)
        }
    }
}

@Composable
private fun SettingsDialogSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
    )
}


@Composable
fun SettingsDialogThemeChooserRow(
    text: String,
    content: @Composable () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            /*.selectable(
                selected = selected,
                role = Role.Switch,
                onClick = onClick,
            )*/
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text)
        content()


    }
}

@Composable
fun DarkModeSwitch(
    checked: Boolean,
    modifier: Modifier = Modifier,
    onCheckedChanged: (Boolean) -> Unit
) {
    val switchWidth = 80.dp    // 160
    val switchHeight = 36.dp    // 64
    val handleSize = 26.dp      // 52
    val handlePadding = 8.dp   //10

    val valueToOffset = if (checked) 1f else 0f
    val offset = remember {
        Animatable(valueToOffset)
    }
    val scope = rememberCoroutineScope()

    DisposableEffect(checked) {
        if (offset.targetValue != valueToOffset) {
            scope.launch {
                offset.animateTo(valueToOffset, animationSpec = tween(1000))

            }
        }
        onDispose { }
    }

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
            .width(switchWidth)
            .height(switchHeight)
            .clip(RoundedCornerShape(switchHeight))
            .background(lerp(BlueSky, NightSky, offset.value))
            .border(3.dp, BorderColor, RoundedCornerShape(switchHeight))
            .toggleable(
                value = checked,
                onValueChange = onCheckedChanged,
                role = Role.Switch,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )
    ) {
        val backgroundPainter = painterResource(R.drawable.background)
        Canvas(modifier = Modifier.fillMaxSize()) {
            with(backgroundPainter) {
                val scale = size.width / intrinsicSize.width
                val scaleHeight = intrinsicSize.height * scale
                translate(top = (size.height - scaleHeight) * (1f - offset.value)) {
                    draw(Size(size.width, scaleHeight))
                }
            }
        }

        Image(
            painter = painterResource(id = R.drawable.glow),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(switchWidth)
                .graphicsLayer {
                    scaleX = 1.2f
                    scaleY = scaleX
                    translationX = lerp(
                        -size.width * 0.5f + handlePadding.toPx() + handleSize.toPx() * 0.5f,
                        switchWidth.toPx() - size.width * 0.5f - handlePadding.toPx() - handleSize.toPx() * 0.5f,
                        offset.value
                    )
                }
        )

        Box(
            modifier = Modifier
                .padding(horizontal = handlePadding)
                .size(handleSize)
                .offset(x = (switchWidth - handleSize - handlePadding * 2f) * offset.value)
                .paint(painterResource(id = R.drawable.sun))
                .clip(CircleShape)
        ) {
            Image(
                painter = painterResource(id = R.drawable.moon), contentDescription = null,
                modifier = Modifier
                    .size(handleSize)
                    .graphicsLayer { translationX = size.width * (1f - offset.value) }
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun SwitchPreview() {
    WelcomeFreshmanTheme {
        Column {
            var value by remember {
                mutableStateOf(true)
            }
            DarkModeSwitch(checked = value, modifier = Modifier.padding(24.dp)) { value = it }
            DarkModeSwitch(checked = !value, modifier = Modifier.padding(24.dp)) { value = it }
        }

    }
}