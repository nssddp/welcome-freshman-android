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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.welcome_freshman.R

/**
 *@date 2024/1/27 10:40
 *@author GFCoder
 */

@Composable
fun ProfileRoute() {
    ProfileScreen()
}

@Composable
fun ProfileScreen() {
    LazyColumn() {
        item {
            PersonalCard(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(160.dp),
                nickName = "昵称",
                department = "计算机与软件学院"
            )
            CommonDivider()
        }

        item {
            CommonCard(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp), cardName = "学生认证"
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
) {

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

    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                AsyncImage(
                    model = selectedImageUri ?: R.drawable.logo, contentDescription = null,
                    contentScale = ContentScale.Crop, modifier = Modifier
                        .size(66.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null
                        ) {
                            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }

                )
                Column(Modifier.padding(start = 12.dp, top = 12.dp)) {
                    Text(text = nickName)
                    Text(text = "学院: $department")
                }

            }

            Row(Modifier.fillMaxWidth(), Arrangement.SpaceAround) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "30")
                    Text(text = "积分")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "30")
                    Text(text = "等级")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "30")
                    Text(text = "成就")
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
            IconButton(onClick = {  }) {
                Icon(imageVector = Icons.Default.ArrowForwardIos, contentDescription = null)
            }
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