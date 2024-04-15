package com.example.welcome_freshman.feature.main.profile

import android.net.Uri
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.welcome_freshman.R
import com.example.welcome_freshman.core.data.model.User
import com.example.welcome_freshman.core.rememberPhotoPicker
import com.example.welcome_freshman.feature.certification.CertificationDialog
import com.example.welcome_freshman.feature.login.LoginViewModel
import com.example.welcome_freshman.feature.updateUserInfo.UpdateViewModel
import com.example.welcome_freshman.feature.updateUserInfo.image2ByteArray
import com.example.welcome_freshman.ui.component.GifImage
import com.example.welcome_freshman.ui.component.PullToReFreshBox
import com.example.welcome_freshman.ui.validToast
import kotlinx.coroutines.launch

/**
 *@date 2024/1/27 10:40
 *@author GFCoder
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileRoute(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    updateViewModel: UpdateViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel(),
    onAuthenticationClick: (String, String) -> Unit,
    onUpdateUserClick: () -> Unit,
    onPointShopClick: () -> Unit,
) {
    LaunchedEffect(Unit) {
        profileViewModel.getUserInfo()
    }

    val profileUiState by profileViewModel.profileUiState.collectAsState()

    val user by profileViewModel.user.collectAsState()

    val adUrl by profileViewModel.adUrl.collectAsState()

    val refreshState = rememberPullToRefreshState()

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    when (profileUiState) {
        ProfileUiState.Loading -> {
            //                item { IndeterminateCircularIndicator() }
            if (!refreshState.isRefreshing) {
                refreshState.startRefresh()
            }

        }

        ProfileUiState.Error -> {
            //                item { NetworkErrorIndicator(onRetryClick = onRetryClick) }
            refreshState.endRefresh()
        }

        is ProfileUiState.Success -> {
            refreshState.endRefresh()

        }
    }
    if (refreshState.isRefreshing) {
        LaunchedEffect(Unit) {
            profileViewModel.getUserInfo()
        }
    }

    ProfileScreen(
        refreshState = refreshState,
        user = user,
        onAuthenticationClick = { it, _ ->
            scope.launch {
                if (!loginViewModel.checkIsValid()) onAuthenticationClick(it, "0000")
                else context.validToast()
            }
        },
        onUpdateUserClick = onUpdateUserClick,
        onRetryClick = { profileViewModel.getUserInfo() },
        uploadAvatar = updateViewModel::uploadAvatar,
        onPointShopClick = onPointShopClick,
        adUrl = adUrl
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    adUrl: String?,
    refreshState: PullToRefreshState,
    user: User?,
    onAuthenticationClick: (String, String?) -> Unit,
    onUpdateUserClick: () -> Unit = {},
    onRetryClick: () -> Unit,
    uploadAvatar: (ByteArray, Int) -> Unit,
    onPointShopClick: () -> Unit,
) {
    var showCertificationDialog by remember {
        mutableStateOf(false)
    }

    if (showCertificationDialog) {
        CertificationDialog(
            onDismiss = { showCertificationDialog = false },
            onAuthenticationClick = { onAuthenticationClick(it, null) }
        )
    }

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    selectedImageUri = user?.avatarUrl?.toUri()
//    Log.d("selectedImageUri", selectedImageUri.toString())

    /*val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                selectedImageUri = uri
                Log.d("PhotoPicker", "Selected URI: $uri")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
    )*/

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val pickMedia = rememberPhotoPicker { uri ->
        if (uri != null) {
            scope.launch {
                if (user?.userId != null) {
                    context.image2ByteArray(uri)?.let { uploadAvatar(it, user.userId) }
                }
            }
//            selectedImageUri = uri
        }
    }

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    if (showBottomSheet) {
        BottomSheet(
            sheetState = sheetState,
            showBottomSheet = { showBottomSheet = it },
            onSelectAvatarClick = {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        )

    }

    PullToReFreshBox(state = refreshState) {

        Image(
            painter = rememberAsyncImagePainter(model = R.drawable.profile_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = .95f
        )

        LazyColumn(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
                PersonalCard(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(225.dp),
                    nickName = user?.userName ?: "",
                    academy = user?.academy ?: "",
                    score = if (user?.score != null) user.score.toString() else "-",
                    grade = user?.grade ?: 0,
                    selectedImageUri = selectedImageUri,
                    onAvatarClick = { showBottomSheet = true },
                    strength = user?.strength?.toString() ?: "-",
                    agility = user?.agility?.toString() ?: "-",
                    intelligence = user?.intelligence?.toString() ?: "-",
                    emp = user?.emp?.toString() ?: "-"
                )
                CommonDivider()
            }

            item {
                CommonCard(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clickable(onClick = { showCertificationDialog = true }),
                    cardName = "学生认证",
                    icon = R.drawable.crystal_shard
                )
                CommonDivider()
            }
            item {
                CommonCard(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clickable(onClick = onUpdateUserClick),
                    cardName = "个人信息",
                    icon = R.drawable.centaur
                )
                CommonDivider()
            }
            item {
                CommonCard(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clickable(onClick = onPointShopClick), cardName = "积分商城",
                    icon = R.drawable.castle
                )
                CommonDivider()
            }
            item {
                Box(Modifier.padding(10.dp)) {

                    Image(
                        painter = rememberAsyncImagePainter(
                            model = adUrl, placeholder = painterResource(
                                id = R.drawable.adpalceholder
                            )
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),
                        contentScale = ContentScale.Crop,
                    )
                    Text(
                        text = "广告",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.background(
                            Color.White.copy(.4f),
                            shape = RoundedCornerShape(3.dp)
                        )
                    )

                }
            }


        }

    }

}

@Composable
fun PersonalCard(
    modifier: Modifier = Modifier,
    nickName: String,
    academy: String,
    score: String,
    grade: Int,
    selectedImageUri: Uri?,
    onAvatarClick: () -> Unit,
    strength: String,
    agility: String,
    intelligence: String,
    emp: String,
) {

    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier,
        color = Color(0xFFE3D6FA)
    ) {
        Column(Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                AsyncImage(
                    model = selectedImageUri ?: R.drawable.logo_yuzu, contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(66.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .clickable(
                            interactionSource = remember {
                                MutableInteractionSource()
                            },
                            indication = null
                        ) {
//                            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            onAvatarClick()
                        }
                )
                Column(Modifier.padding(start = 12.dp, top = 8.dp)) {
                    Text(text = nickName, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "学院: $academy",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.width(50.dp))
                GifImage(painter = R.drawable.midouzi, modifier = Modifier.size(80.dp))

            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AttrItem(painter = R.drawable.strength_icon, attrName = "力量", value = strength)
                AttrItem(painter = R.drawable.agility_icon, attrName = "敏捷", value = agility)
                AttrItem(
                    painter = R.drawable.intelligence_icon,
                    attrName = "智慧",
                    value = intelligence
                )
            }
            LinearProgressIndicator(progress = { 0.6f },modifier= Modifier.padding(horizontal = 14.dp).padding(top = 3.dp).fillMaxWidth())
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = "下一级还需经验: 3300",
                    color = Color.Gray,
                    style = MaterialTheme.typography.labelMedium,
                    modifier= Modifier.padding(horizontal = 10.dp)
                )
                Text(
                    text = "10000/13300",
                    color = Color.Gray,
                    style = MaterialTheme.typography.labelMedium,
                    modifier= Modifier.padding(horizontal = 10.dp)
                )


            }


            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = score, style = MaterialTheme.typography.bodyMedium)
                    Text(text = "积分", style = MaterialTheme.typography.bodyMedium)
                }
                VerticalDivider(
                    modifier = Modifier
                        .fillMaxHeight(0.3f)
                        .width(1.dp)
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = grade.toString(), style = MaterialTheme.typography.bodyMedium)
                    Text(text = "等级", style = MaterialTheme.typography.bodyMedium)
                }
                VerticalDivider(
                    modifier = Modifier
                        .fillMaxHeight(0.3f)
                        .width(1.dp)
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "-", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "成就", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

    }
}

@Composable
fun AttrItem(@DrawableRes painter: Int, attrName: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = painter),
            contentDescription = null,
            Modifier.size(18.dp)
        )
        Text(text = "$attrName: ", style = MaterialTheme.typography.labelMedium)
        Text(text = value, style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
fun CommonCard(
    modifier: Modifier = Modifier,
    cardName: String,
    icon: Int
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
    ) {
        Row(
            Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 5.dp)
            )
            Text(text = cardName)
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun CommonDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 8.dp),
        color = Color.Transparent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    sheetState: SheetState,
    showBottomSheet: (Boolean) -> Unit,
    onSelectAvatarClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    ModalBottomSheet(
        onDismissRequest = { showBottomSheet(false) },
        sheetState = sheetState,
        dragHandle = {}
    ) {
        /*Button(onClick = {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) {
                    showBottomSheet(false)
                }
            }
        }) {
            Text("Hide bottom sheet")
        }*/
        Column(
            Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextButton(onClick = {}, Modifier.fillMaxWidth()) {
                Text("view avatar")
            }
            HorizontalDivider()
            TextButton(onClick = {
                onSelectAvatarClick()
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showBottomSheet(false)
                    }
                }
            }, Modifier.fillMaxWidth()) {
                Text("change avatar")
            }
            HorizontalDivider()
            TextButton(
                onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet(false)
                        }
                    }
                },
                Modifier.fillMaxWidth()
            ) {
                Text("Hide bottom sheet")

            }

        }

    }

}


@Preview(showBackground = true)
@Composable
fun profilePreview() {
    /*ProfileScreen(
        profileUiState = ProfileUiState.Success(User(1, "1", "1", "1", 1, 1, "1")),
        onRetryClick = {})*/
}