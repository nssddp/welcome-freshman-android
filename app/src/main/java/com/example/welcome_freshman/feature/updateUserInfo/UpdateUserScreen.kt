package com.example.welcome_freshman.feature.updateUserInfo

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Brush
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.welcome_freshman.R
import com.example.welcome_freshman.core.data.model.User
import com.example.welcome_freshman.core.rememberPhotoPicker
import com.example.welcome_freshman.feature.main.profile.BottomSheet
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

/**
 *@date 2024/3/6 19:45
 *@author GFCoder
 */

@Composable
fun UpdateUserRoute(onBackClick: () -> Unit, viewModel: UpdateViewModel = hiltViewModel()) {
    var isUpdateName by remember {
        mutableStateOf(false)
    }

    val uiState by viewModel.uiState.collectAsState()

    val avatarUri by viewModel.avatarUri.collectAsState()

    val scope = rememberCoroutineScope()

    when (uiState) {
        UpdateUiState.Loading -> {


        }

        is UpdateUiState.Success -> {
            var user by remember {
                mutableStateOf((uiState as UpdateUiState.Success).user)
            }
            UpdateUserScreen(
                user = user,
                avatarUri = avatarUri,
                onBackClick = onBackClick,
                onUpdateNameClick = { trigger -> isUpdateName = trigger },
                onGenderUpdate = {
                    scope.launch {
                        user = user.copy(
                            gender = it
                        )
                        Log.d("updateuserInfo", user.toString())
                        viewModel.updateUserInfo(user)
                    }

                },
                uploadAvatar = {
                    user.userId?.let { userId ->
                        viewModel.uploadAvatar(
                            it,
                            userId
                        )
                    }
                }

            )
            /*if (!isUpdateName) {

            } else {
                UpdateNameScreen(
                    onBackClick = { isUpdateName = it },
                    nickname = { user.userName },
                    onConfirmClick = {
                        // 进行网络请求
                        scope.launch {
                            user = user.copy(
                                userName = it
                            )
                            viewModel.updateUserInfo(user)
                        }
                        isUpdateName = false
                    },
                )
            }*/
        }

        else -> {

        }
    }
    BackHandler(enabled = isUpdateName) {
        isUpdateName = false
    }

}

/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateNameScreen(
    onBackClick: (Boolean) -> Unit = {},
    nickname: () -> String,
    onConfirmClick: (userName: String) -> Unit = {},
) {
    var value by remember {
        mutableStateOf(nickname())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "修改昵称", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { onBackClick(false) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    TextButton(onClick = { onConfirmClick(value) }) {
                        Text(text = "保存", style = MaterialTheme.typography.titleMedium)
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding), contentAlignment = Alignment.TopCenter
        ) {

            TextField(
                value = value,
                onValueChange = {
                    value = it
                },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
        }
    }

}*/


@Preview
@Composable
fun UpdateNameScreenPreview() {
//    UpdateNameScreen(nickname = { "" })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateUserScreen(
    user: User,
    avatarUri: String?,
    onBackClick: () -> Unit = {},
    onUpdateNameClick: (Boolean) -> Unit,
    onGenderUpdate: (String) -> Unit,
    uploadAvatar: (ByteArray?) -> Unit,
) {
    Log.d("updateUser", user.toString())

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "个人资料", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding), horizontalAlignment = Alignment.CenterHorizontally) {
            var selectedImageUri by remember {
                mutableStateOf<Uri?>(user.avatarUrl?.toUri())
            }
            if (!avatarUri.isNullOrBlank()) selectedImageUri = avatarUri.toUri()
            val pickMedia = rememberPhotoPicker(onImagePicked = { uri ->
                if (uri != null) {
                    scope.launch {
                        uploadAvatar(context.image2ByteArray(uri))
                    }
//                    selectedImageUri = uri
                }
            })

            val sheetState = rememberModalBottomSheetState()
            val selectedGender by remember {
                mutableStateOf(user.gender)
            }
            // 性别选择
            /*var showGenderBottomSheet by remember { mutableStateOf(false) }

            if (showGenderBottomSheet) {
                UpdateUserBottomSheet(
                    sheetState = sheetState,
                    showBottomSheet = { showGenderBottomSheet = it },
                    selectedGender = { gender ->
                        selectedGender = gender
                        onGenderUpdate(gender)
                    },
                    gender = { selectedGender }
                )
            }*/
            // 头像选择
            var showAvatarBottomSheet by remember { mutableStateOf(false) }
            if (showAvatarBottomSheet) {
                BottomSheet(
                    sheetState = sheetState,
                    showBottomSheet = { showAvatarBottomSheet = it },
                    onSelectAvatarClick = {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
                )

            }

            Box(
                Modifier
                    .fillMaxWidth()
                    .background(Color(0xF0ECECEC)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = selectedImageUri ?: R.drawable.logo_yuzu, contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(16.dp)
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .clickable(
                            interactionSource = remember {
                                MutableInteractionSource()
                            },
                            indication = null
                        ) {
                            showAvatarBottomSheet = true
                            //                        onAvatarClick()
                        }
                )
                Icon(
                    imageVector = Icons.Rounded.Brush,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 75.dp, bottom = 20.dp)
                        .shadow(
                            elevation = 3.dp,
                            shape = CircleShape,
                            clip = true
                        )
                        .align(Alignment.BottomCenter)
                        .background(Color.White)
                        .size(24.dp)

                )
            }

            HorizontalDivider(Modifier.padding(horizontal = 8.dp), thickness = 0.8.dp)
            UpdateCard(
                cardName = "昵称",
                userInfo = user.userName,
                modifier = Modifier
                    .fillMaxWidth()
                /*.clickable(
                    remember {
                        MutableInteractionSource()
                    }, indication = null
                ) {
                    onUpdateNameClick(true)
                }*/
            )

            UpdateCard(
                cardName = "学院",
                userInfo = user.academy ?: "",
                modifier = Modifier.fillMaxWidth()
            )



            UpdateCard(
                cardName = "性别",
                userInfo = selectedGender,
                modifier = Modifier
                    .fillMaxWidth()
                /*.clickable(remember { MutableInteractionSource() }, indication = null) {
                    showGenderBottomSheet = true
                }*/
            )


//            UpdateCard(cardName = "")
//            UpdateCard(cardName = "")
        }

    }
}


@Composable
private fun UpdateCard(
    modifier: Modifier = Modifier,
    cardName: String,
    userInfo: String
) {
    Column(modifier = modifier) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = modifier
        ) {
            Row(
                Modifier.padding(horizontal = 12.dp, vertical = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = cardName, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = userInfo,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                /*Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                }*/

            }
        }
        HorizontalDivider(Modifier.padding(horizontal = 8.dp), thickness = 0.8.dp)
    }
}


/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateUserBottomSheet(
    sheetState: SheetState,
    showBottomSheet: (Boolean) -> Unit,
    selectedGender: (String) -> Unit,
    gender: () -> String,
) {
    val scope = rememberCoroutineScope()
    var defaultGender by remember {
        mutableStateOf("男")
    }
    ModalBottomSheet(
        onDismissRequest = { showBottomSheet(false) },
        sheetState = sheetState,
        dragHandle = {},
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp), horizontalArrangement = Arrangement.SpaceAround
            ) {
                TextButton(onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet(false)
                        }
                    }
                }) {
                    Text(text = "取消", style = MaterialTheme.typography.titleMedium)
                }
                Text(text = "选择性别", style = MaterialTheme.typography.titleMedium)

                TextButton(onClick = {
                    selectedGender(defaultGender)
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet(false)
                        }
                    }
                }) {
                    Text(text = "确定", style = MaterialTheme.typography.titleMedium)
                }
            }
            GenderPicker(
                selectedGender = { gender ->
                    defaultGender = gender
                },
                gender = gender

            )
        }

    }
}

@Composable
fun GenderPicker(selectedGender: (String) -> Unit, gender: () -> String) {
    val radioOptions = listOf("男", "女")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(gender()) }
    // Note that Modifier.selectableGroup() is essential to ensure correct accessibility behavior
    Column(Modifier.selectableGroup()) {
        radioOptions.forEach { text ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = {
                            onOptionSelected(text)
                            selectedGender(text)
                        },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = null // null recommended for accessibility with screenreaders
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}*/

fun Context.image2ByteArray(imageUri: Uri): ByteArray? {
    return try {
        this.contentResolver.openInputStream(imageUri)?.use { inputStream ->
            val outputStream = ByteArrayOutputStream()
            inputStream.copyTo(outputStream)
            outputStream.toByteArray()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


@Preview(showBackground = true)
@Composable
fun UpdatePreview() {
//    UpdateUserScreen()
}