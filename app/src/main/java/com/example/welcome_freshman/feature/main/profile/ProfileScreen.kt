package com.example.welcome_freshman.feature.main.profile

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.welcome_freshman.R
import com.example.welcome_freshman.feature.certification.CertificationDialog
import kotlinx.coroutines.launch

/**
 *@date 2024/1/27 10:40
 *@author GFCoder
 */

@Composable
fun ProfileRoute(onAuthenticationClick: () -> Unit) {

    ProfileScreen(onAuthenticationClick = onAuthenticationClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onAuthenticationClick: () -> Unit = {}) {
    var showCertificationDialog by remember {
        mutableStateOf(false)
    }

    if (showCertificationDialog) {
        CertificationDialog(
            onDismiss = { showCertificationDialog = false },
            onFaceAuthenticationClick = onAuthenticationClick
        )
    }

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                selectedImageUri = uri
                Log.d("PhotoPicker", "Selected URI: $uri")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
    )

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    if (showBottomSheet) {
        bottomSheet(
            sheetState = sheetState,
            showBottomSheet = { showBottomSheet = it },
            onSelectAvatarClick = {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        )

    }

    LazyColumn() {
        item {
            PersonalCard(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(175.dp),
                nickName = "昵称",
                department = "计算机与软件学院",
                selectedImageUri = selectedImageUri,
                onAvatarClick = { showBottomSheet = true }
            )
            CommonDivider()
        }

        item {
            CommonCard(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable(onClick = { showCertificationDialog = true }),
                cardName = "学生认证"
            )
            CommonDivider()
        }
        item {
            CommonCard(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp), cardName = "个人信息修改"
            )
            CommonDivider()
        }
        item {
            CommonCard(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp), cardName = "积分商城"
            )
            CommonDivider()
        }

    }

}

@Composable
fun PersonalCard(
    modifier: Modifier = Modifier,
    nickName: String,
    department: String,
    selectedImageUri: Uri?,
    onAvatarClick: () -> Unit,
) {


    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
    ) {
        Column(Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                AsyncImage(
                    model = selectedImageUri ?: R.drawable.logo, contentDescription = null,
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
                        text = "学院: $department",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "30", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "积分", style = MaterialTheme.typography.bodyMedium)
                }
                Divider(
                    modifier = Modifier
                        .fillMaxHeight(0.3f)
                        .width(1.dp)
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "30", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "等级", style = MaterialTheme.typography.bodyMedium)
                }
                Divider(
                    modifier = Modifier
                        .fillMaxHeight(0.3f)
                        .width(1.dp)
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "30", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "成就", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

    }
}

@Composable
fun CommonCard(
    modifier: Modifier = Modifier,
    cardName: String,
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
    ) {
        Row(
            Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = cardName)
            Icon(imageVector = Icons.Default.ArrowForwardIos, contentDescription = null)
        }
    }
}

@Composable
fun CommonDivider() {
    Divider(
        color = Color.Transparent,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun bottomSheet(
    sheetState: SheetState,
    showBottomSheet: (Boolean) -> Unit,
    onSelectAvatarClick: () -> Unit
) {
    /*BottomSheetScaffold(sheetContent = ) {
        
    }*/

    val scope = rememberCoroutineScope()
    ModalBottomSheet(
        onDismissRequest = { showBottomSheet(false) },
        sheetState = sheetState
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
            Divider()
            TextButton(onClick = {}, Modifier.fillMaxWidth()) {
                Text("view avatar")
            }
            Divider()
            TextButton(onClick = onSelectAvatarClick, Modifier.fillMaxWidth()) {
                Text("change avatar")
            }
            Divider()
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
    ProfileScreen()
}