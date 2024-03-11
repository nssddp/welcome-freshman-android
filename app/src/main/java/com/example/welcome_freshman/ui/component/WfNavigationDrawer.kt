package com.example.welcome_freshman.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 *@date 2024/1/30 14:15
 *@author GFCoder
 */
@Composable
fun WfNavigationDrawer(
    drawerState: DrawerState,
    gesturesEnabled: () -> Boolean,
    onLogoutClick: () -> Unit,
    onCheckClick: () -> Unit,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = gesturesEnabled(),
        drawerContent = {
            ModalDrawerSheet {
                Text(text = "侧边栏", modifier = Modifier.padding(16.dp))
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text(text = "退出登录") },
                    selected = false,
                    onClick = onLogoutClick
                )
                NavigationDrawerItem(
                    label = { Text(text = "身份认证") },
                    selected = false,
                    onClick = onCheckClick
                )

            }
        }
    ) {
        content()
    }
}