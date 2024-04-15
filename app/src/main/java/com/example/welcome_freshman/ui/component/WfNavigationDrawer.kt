package com.example.welcome_freshman.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemColors
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.welcome_freshman.R

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
    onMapClick: () -> Unit,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = gesturesEnabled(),
        drawerContent = {

            Box {
                Image(
                    painter = painterResource(id = R.drawable.side_drawer_background),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = 70.dp),
                    contentScale = ContentScale.Crop
                )

            }
            ModalDrawerSheet(
                Modifier.padding(end = 70.dp),
                drawerContainerColor = Color.Transparent
            ) {

                Text(text = "侧边栏", modifier = Modifier
                    .padding(16.dp)
                    .background(Color.White.copy(.8f)))
                HorizontalDivider()

                NavigationDrawerItem(
                    label = { Text(text = "身份认证") },
                    selected = false,
                    onClick = onCheckClick,
                    icon = {
                      Image(painter = painterResource(id = R.drawable.villager), contentDescription = null)
                    },
                    colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.White.copy(0.7f))
                )
                Spacer(modifier = Modifier.height(6.dp))
                NavigationDrawerItem(
                    label = { Text(text = "任务地图") },
                    selected = false,
                    onClick = onMapClick,
                    icon = {
                        Image(painter = painterResource(id = R.drawable.adventure_map), contentDescription = null)
                    },
                    colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.White.copy(0.7f))
                )
                Spacer(modifier = Modifier.height(6.dp))
                NavigationDrawerItem(
                        label = { Text(text = "退出登录") },
                selected = false,
                onClick = onLogoutClick,
                    icon = {
                        Image(painter = painterResource(id = R.drawable.grim_reaper), contentDescription = null)
                    },
                colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.White.copy(0.7f))
                )
            }

        }
    ) {
        content()
    }
}