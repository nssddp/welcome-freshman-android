package com.example.welcome_freshman.feature.comment

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.welcome_freshman.R
import com.example.welcome_freshman.core.data.model.Comment
import com.example.welcome_freshman.ui.component.IndeterminateCircularIndicator
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 *@date 2024/4/3 16:10
 *@author GFCoder
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentBottomSheet(
//    openBottomSheet: Boolean,
    taskId: Int,
    openBottomSheetChanged: (Boolean) -> Unit,
    viewModel: CommentViewModel = hiltViewModel()
) {

    val commentUiState by viewModel.commentUiState.collectAsState()

//    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    var skipPartiallyExpanded by remember { mutableStateOf(false) }
    val edgeToEdgeEnabled by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    // App content
    /*Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            Modifier.toggleable(
                value = skipPartiallyExpanded,
                role = Role.Checkbox,
                onValueChange = { checked -> skipPartiallyExpanded = checked }
            )
        ) {
            Checkbox(checked = skipPartiallyExpanded, onCheckedChange = null)
            Spacer(Modifier.width(16.dp))
            Text("Skip partially expanded State")
        }
        Row(
            Modifier.toggleable(
                value = edgeToEdgeEnabled,
                role = Role.Checkbox,
                onValueChange = { checked -> edgeToEdgeEnabled = checked }
            )
        ) {
            Checkbox(checked = edgeToEdgeEnabled, onCheckedChange = null)
            Spacer(Modifier.width(16.dp))
            Text("Toggle edge to edge enabled.")
        }
        Button(onClick = { openBottomSheet = !openBottomSheet }) {
            Text(text = "Show Bottom Sheet")
        }
    }*/

    // Sheet content

    viewModel.getComment(taskId)


    val windowInsets = if (edgeToEdgeEnabled)
        WindowInsets(0) else BottomSheetDefaults.windowInsets

    ModalBottomSheet(
        onDismissRequest = { openBottomSheetChanged(false) },
        sheetState = bottomSheetState,
        windowInsets = windowInsets
    ) {
//            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        /* Button(
             // Note: If you provide logic outside of onDismissRequest to remove the sheet,
             // you must additionally handle intended state cleanup, if any.
             onClick = {
                 scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                     if (!bottomSheetState.isVisible) {
                         openBottomSheet = false
                     }
                 }
             }
         ) {
             Text("Hide Bottom Sheet")
         }
     }*/
        Text(
            text = "评论区",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            textAlign = TextAlign.Center
        )
        when (commentUiState) {
            CommentUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    IndeterminateCircularIndicator(Modifier.align(Alignment.Center))
                }
            }

            CommentUiState.Error -> {}
            is CommentUiState.Success -> {
                val comments = (commentUiState as CommentUiState.Success).comments

                HorizontalDivider()

                LazyColumn {
                    items(comments, key = { it.commentId!! }) {
                        CommentBox(comment = it)
                        HorizontalDivider()
                    }
                }
            }

        }


    }


}

@Composable
fun CommentBox(comment: Comment?) {

    var isExpanded by remember {
        mutableStateOf(false)
    }

    var count by remember {
        mutableStateOf(0)
    }
    var isTapThumb by remember {
        mutableStateOf(false)
    }

    Column(
        Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .animateContentSize(animationSpec = spring(stiffness = Spring.StiffnessHigh))
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = /*user?.avatarUrl?:*/ painterResource(id = R.drawable.logo_yuzu),
                contentDescription = "Artist image",
                modifier = Modifier.size(45.dp)
            )
            Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                Text(
                    comment?.account ?: "Wander Dan",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Serif
                )
                Text(
                    comment?.createTime?.let { formatTimestamp(it) } ?: "",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.weight(0.1f))
            Icon(
                imageVector = if (isTapThumb) Icons.Filled.ThumbUp else
                    Icons.Outlined.ThumbUp,
                contentDescription = null,
                tint = if (isTapThumb) Color.Black else
                    Color.Gray,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    isTapThumb = !isTapThumb
                    count = if (isTapThumb)
                        count + 1
                    else count - 1
                }
            )
            if (comment != null) {
                Text(
                    text = "${comment.praise?.plus(count)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isTapThumb) Color.Black else
                        Color.Gray,
                    modifier = Modifier.padding(start = 3.dp)
                )
            }
        }
        Spacer(Modifier.height(16.dp))

        ExpandableText(text = comment?.content ?: "wtf".repeat(50))

    }

}

@Composable
fun ExpandableText(text: String, maxLines: Int = 2) {
    var isExpanded by remember { mutableStateOf(false) }
//    val textLines = text.split("\n")
    var isOverflow by remember {
        mutableStateOf(false)
    }

    Column {
        Text(
            text = text,
            maxLines = if (isExpanded) Int.MAX_VALUE else maxLines,
//            overflow = TextOverflow.Ellipsis,
            onTextLayout = {
                isOverflow = it.didOverflowHeight
            },
            lineHeight = 20.sp,
            style = MaterialTheme.typography.bodyMedium
        )
        if (isOverflow || isExpanded) {
            ClickableText(
                text = AnnotatedString(if (isExpanded) "收起" else "展开"),
                onClick = { isExpanded = !isExpanded },
                style = TextStyle(color = Color.Blue)
            )
        }
    }
}

fun formatTimestamp(timestamp: Long): String {
    val currentTime = System.currentTimeMillis() / 1000
    val diff = currentTime - timestamp

    return when {
        diff < 60 -> "刚刚"
        diff < 3600 -> "${diff / 60} 分钟前"
        diff < 86400 -> "${diff / 3600} 小时前"
        else -> {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            dateFormat.format(Date(timestamp * 1000))
        }
    }
}


// Preview the composable in the IDE (optional)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
//    ModalBottomSheetSample()
    CommentBox(comment = null)
}


/*
Icon(
imageVector = if (isExpanded) Icons.Rounded.ArrowDropUp
else Icons.Rounded.ArrowDropDown,
contentDescription = null,
modifier = Modifier
.clickable(
interactionSource = remember { MutableInteractionSource() },
indication = null
) { isExpanded = !isExpanded }

)*/
