package com.example.welcome_freshman.feature.main.rank

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.welcome_freshman.R
import com.example.welcome_freshman.core.data.model.SingleTaskRank
import com.example.welcome_freshman.core.data.model.User
import com.example.welcome_freshman.ui.component.NetworkErrorIndicator
import com.example.welcome_freshman.ui.component.PullToReFreshBox
import com.example.welcome_freshman.ui.theme.WelcomeFreshmanTheme
import kotlinx.coroutines.launch

/**
 *@date 2024/1/27 10:49
 *@author GFCoder
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RankRoute(viewModel: RankViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    val refreshState = rememberPullToRefreshState()


    RankScreen(
        uiState = uiState,
        onRetryClick = { viewModel.getRanks(it) },
        refreshState = refreshState,
        onGetRank = { viewModel.getRanks(it) })
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RankScreen(
    uiState: RankUiState,
    onRetryClick: (Int) -> Unit,
    refreshState: PullToRefreshState,
    onGetRank: (Int) -> Unit
) {
    val tabList = listOf("积分排行榜", "用时最短排行榜", "单项任务排行榜")
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState {
        tabList.size
    }
    val selectedTabIndex by remember {
        derivedStateOf { pagerState.currentPage }
    }

    if (refreshState.isRefreshing) {
        LaunchedEffect(true) {
//            delay(3000)
            onRetryClick(selectedTabIndex)
        }
    }
    LaunchedEffect(selectedTabIndex) {
        onGetRank(selectedTabIndex)
    }
    PullToReFreshBox(state = refreshState) {

        Image(
            painter = rememberAsyncImagePainter(model = R.drawable.rank_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.95f
        )

        Column(modifier = Modifier.fillMaxSize()) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabList.fastForEachIndexed { index, currentTab ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = { Text(text = currentTab) }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
//                contentPadding = PaddingValues(top = 0.dp),
                modifier = Modifier
                    .fillMaxSize()
            ) { index ->
                /*Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column {
                        Text(text = "This is ${tabList[selectedTabIndex]}")
                        Button(onClick = onDetailClick) {
                            Text(text = "go to detail")
                        }
                    }

                }*/
                when (uiState) {
                    RankUiState.Loading -> {

                        refreshState.startRefresh()

                        onGetRank(selectedTabIndex)
//                        IndeterminateCircularIndicator()
                    }

                    RankUiState.Error -> {
                        refreshState.endRefresh()
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            NetworkErrorIndicator(onRetryClick = { onRetryClick(index) })
                        }
                    }

                    is RankUiState.Success -> {
                        refreshState.endRefresh()

                        val ranks = uiState.rankData

                        /* LazyColumn(Modifier.fillMaxSize()) {
                             item {
                                 Box(
                                     modifier = Modifier
                                         .fillMaxWidth()
                                         .padding(bottom = 12.dp),
                                     contentAlignment = Alignment.Center
                                 ) {
                                     OutRankItem(
                                         modifier = Modifier
                                             .padding(top = 26.dp, end = 168.dp),
                                         rank[1].userName ?: "I'm 第二名",
                                         rankIcon = R.drawable.rank_second,
                                         score = rank[1].score.toString(),
                                         rankAvatarBox = R.drawable.avatar_box2,
                                         brush = Brush.linearGradient(
                                             colorStops = arrayOf(
                                                 0.0f to Color(0xFF616566),
                                                 0.6f to Color(0xFFAFCCCB),
                                             )
                                         )
                                     )
                                     OutRankItem(
                                         modifier = Modifier
                                             .padding(top = 26.dp, start = 168.dp),
                                         rank[2].userName ?: "I'm 第三名",
                                         rankIcon = R.drawable.rank_third,
                                         rankAvatarBox = R.drawable.avatar_box3,
                                         score = rank[2].score.toString(),
                                         brush = Brush.linearGradient(
                                             colorStops = arrayOf(
                                                 0.0f to Color(0xFF9F3311),
                                                 0.6f to Color(0xFFFADD76),
                                             )
                                         )
                                     )
                                     OutRankItem(
                                         nickName = rank[0].userName ?: "I'm 第一名",
                                         rankIcon = R.drawable.rank_first,
                                         rankAvatarBox = R.drawable.avatar_box1,
                                         score = rank[0].score.toString(),
                                         brush = Brush.linearGradient(
                                             colorStops = arrayOf(
                                                 0.0f to Color(0xFFFFF500),
                                                 0.6f to Color(0xFFFFB800),
                                             )
                                         )
                                     )
                                 }
                             }
                             item {
                                 RankItem(
                                     modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer),
                                     rank = "排名",
                                     nickName = "姓名",
                                     score = "积分"
                                 )
                             }

                             for (i in 4 until rank.size) {
                                 item {
                                     HorizontalDivider(
 //                                Modifier.padding(vertical = 3.dp),
                                         thickness = 1.dp,
                                         color = MaterialTheme.colorScheme.primaryContainer
                                     )
                                 }
                                 item(key = i) {
                                     RankItem(
                                         rank = i.toString(),
                                         nickName = rank[i].userName,
                                         score = rank[i].score.toString()
                                     )
                                 }

                             }
                             item { CommonDivider() }
                         }*/
                        if (selectedTabIndex != 2 && ranks.ranks != null)
                            NewRankList(ranks = ranks.ranks, index = selectedTabIndex)
                        else if (ranks.rankShort != null) {
                            var expanded by remember { mutableStateOf(false) }
                            var selectedRankIndex by remember {
                                mutableIntStateOf(0)
                            }
                            Box {

//                                Spacer(modifier = Modifier.height(15.dp))
                                NewRankList(
                                    ranks = ranks.rankShort[selectedRankIndex].userCompletions,
                                    index = selectedTabIndex
                                )

                                RankDropdownMenu(
                                    ranks = ranks.rankShort,
                                    expanded = expanded,
                                    expandedChanged = { isExpanded ->
                                        expanded = isExpanded
                                    }
                                ) { rankIndex ->
                                    selectedRankIndex = rankIndex
                                }

                            }


                        }

                    }
                }


            }
        }


    }
}

@Composable
fun RankItem(
    modifier: Modifier = Modifier,
    nickName: String = "无名氏",
    rank: String,
    score: String?,
) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .padding(vertical = 12.dp),
        Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = rank,
            Modifier.width(30.dp),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Text(
            text = nickName,
            modifier = Modifier.width(70.dp),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
        Spacer(modifier = Modifier.weight(0.6f))
        Text(
            text = score ?: "",
            modifier = Modifier.width(50.dp),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
        )
        /*if (rankIcon != null) {
            Image(painter = painterResource(id = rankIcon), contentDescription = null)
        } */
    }
}

@Composable
fun OutRankItem(
    modifier: Modifier = Modifier,
    nickName: String,
    score: String,
    @DrawableRes rankIcon: Int,
    @DrawableRes rankAvatarBox: Int,
    brush: Brush,
) {
    /*Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.medium
            )
            .size(95.dp, 145.dp),
    ) {*/

//    background: linear-gradient(220.55deg, #AFCCCB 0%, #616566 100%);


    ElevatedCard(
//        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = MaterialTheme.shapes.medium,
//        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = modifier
            .size(95.dp, 145.dp)
            .background(
                brush = brush,
                shape = MaterialTheme.shapes.medium
            ),
    ) {
        Column(
            Modifier
                .padding(top = 6.dp)
                .fillMaxSize()
                .background(color = Color.Transparent),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(60.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(rankAvatarBox),
                    contentDescription = null,
                    modifier = Modifier.size(61.dp)
                )
                AsyncImage(
                    model = R.drawable.logo_yuzu, contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(start = 8.5.dp, top = 8.dp)
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {}
                )
            }

            Text(text = nickName, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = score, style = MaterialTheme.typography.bodyMedium)
            Image(painter = painterResource(rankIcon), contentDescription = null)

        }
    }


//    }
}

data class Rank(
    val nickName: String
)


@Composable
fun NewRankList(ranks: List<User>, index: Int) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp)
    ) {
        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = if (index == 2) 30.dp else 0.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                when (ranks.size) {

                    1 -> {
                        TopThreeItem(
                            index = index,
                            rank = ranks[0],
                            rankIcon = R.drawable.king,
                            onItemClick = {}
                        )
                    }

                    2 -> {
                        TopThreeItem(
                            index = index,
                            rank = ranks[1],
                            isTop = false,
                            rankNum = "2",
                            rankIcon = R.drawable.knight,
                            headColor = Color(0XFFBFBFC0),
                            pointColor = Color(0XFFBFBFC0),
                            onItemClick = {}
                        )
                        TopThreeItem(
                            index = index,
                            rank = ranks[0],
                            rankIcon = R.drawable.king,
                            onItemClick = {}
                        )


                    }

                    3 -> {
                        TopThreeItem(
                            index = index,
                            rank = ranks[1],
                            isTop = false,
                            rankNum = "2",
                            rankIcon = R.drawable.knight,
                            headColor = Color(0XFFBFBFC0),
                            pointColor = Color(0XFFBFBFC0),
                            onItemClick = {}
                        )
                        TopThreeItem(
                            index = index,
                            rank = ranks[0],
                            rankIcon = R.drawable.king,
                            onItemClick = {}
                        )

                        TopThreeItem(
                            index = index,
                            rank = ranks[2],
                            isTop = false,
                            rankNum = "3",
                            rankIcon = R.drawable.viking,
                            headColor = Color(0XFFAE844F),
                            pointColor = Color(0XFFAE844F),
                            onItemClick = {}
                        )
                    }

                    else -> {
                        TopThreeItem(
                            index = index,
                            rank = ranks[1],
                            isTop = false,
                            rankNum = "2",
                            rankIcon = R.drawable.knight,
                            headColor = Color(0XFFBFBFC0),
                            pointColor = Color(0XFFBFBFC0),
                            onItemClick = {}
                        )
                        TopThreeItem(
                            index = index,
                            rank = ranks[0],
                            rankIcon = R.drawable.king,
                            onItemClick = {}
                        )

                        TopThreeItem(
                            index = index,
                            rank = ranks[2],
                            isTop = false,
                            rankNum = "3",
                            rankIcon = R.drawable.viking,
                            headColor = Color(0XFFAE844F),
                            pointColor = Color(0XFFAE844F),
                            onItemClick = {}
                        )
                    }

                }


            }
        }
        item { Spacer(modifier = Modifier.height(24.dp)) }
        if (ranks.size > 3)
            items(ranks.subList(3, ranks.size), key = { it.userId!! }) {
                OutTopRankItem(rank = it, index = index, onItemClick = {})
            }

    }
}

@Composable
fun TopThreeItem(
    index: Int,
    isTop: Boolean = true,
    rankNum: String = "1",
    @DrawableRes rankIcon: Int,
    headColor: Color = Color(0XFFFEBB3A),
    pointColor: Color = Color(0XFFF8C96F),
    rank: User,
    onItemClick: () -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // 头像
        Box {
            AsyncImage(
                model = rank.avatarUrl ?: R.drawable.logo_yuzu,
                contentDescription = null,
                modifier = Modifier
                    .padding(top = if (isTop) 21.dp else 58.dp)
                    .size(if (isTop) 137.dp else 100.dp)
                    .border(BorderStroke(3.dp, headColor), CircleShape)
                    .padding(2.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            if (isTop)
            // 皇冠
                Image(
                    painter = painterResource(id = R.drawable.huang_guan),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                )
            // 排名
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(top = if (isTop) 145.dp else 148.dp)
            ) {
                /*Text(
                    text = rankNum,
                    modifier = Modifier
                        .size(if (isTop) 28.dp else 21.dp)
                        .clip(CircleShape)
                        .background(headColor),
                    color = Color.White,
                    fontSize = if (isTop) 23.sp else 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )*/
                Image(
                    painter = painterResource(id = rankIcon), contentDescription = null,
                    modifier = Modifier
                        .size(if (isTop) 28.dp else 21.dp)
                        .clip(CircleShape)
                        .background(headColor),
                )
            }
        }
        // 名字
        Text(
            text = rank.userName,
            modifier = Modifier.padding(top = 12.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        // 底部积分部分
        Row(
            Modifier
                .padding(top = 7.dp)
                .clip(MaterialTheme.shapes.large)
                .height(30.dp)
                .background(pointColor),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(
                    id = when (index) {
                        0 -> R.drawable.point
                        1 -> R.drawable.ave_time
                        else -> R.drawable.speed_rank_icon
                    }
                ),
                contentDescription = null,
            )
            Text(
                text = "${
                    when (index) {
                        0 -> rank.score
                        1 -> {
                            val time = String.format("%.2f", rank.aveTime?.toDouble()?.div(3600))
                            "$time 小时"
                        }

                        else -> {
                            val time =
                                String.format("%.2f", rank.completionTime?.toDouble()?.div(3600))
                            "$time 小时"
                        }
                    }
                }",
                modifier = Modifier.padding(start = 4.dp, end = 6.dp),
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun OutTopRankItem(rank: User, index: Int, onItemClick: () -> Unit) {

    var showDialog by remember {
        mutableStateOf(false)
    }

    if (showDialog)
        RankDialog(user = rank) {
            showDialog = false
        }

    Surface(
        modifier = Modifier
            .padding(8.dp)
            .height(70.dp)
            .clickable {
                showDialog = true

            },
        shadowElevation = 3.dp,
        shape = MaterialTheme.shapes.medium,
        color = Color.White.copy(.6f)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(// 排名
                text = "${rank.rank}",
                modifier = Modifier.padding(start = 16.dp, end = 8.dp)
            )

            AsyncImage(// 头像
                model = rank.avatarUrl ?: R.drawable.logo_yuzu,
                contentDescription = null,
                modifier = Modifier
                    .size(57.dp)
                    .border(2.dp, Color(0XFFAE844F), CircleShape)
                    .clip(CircleShape)
                    .padding(2.dp),
                contentScale = ContentScale.Crop
            )
            Text(text = rank.userName) // 名字
            Spacer(modifier = Modifier.weight(1f))
            // 积分
            Image(
                painter = painterResource(
                    id = when (index) {
                        0 -> R.drawable.point
                        1 -> R.drawable.ave_time
                        else -> R.drawable.speed_rank_icon
                    }
                ),
                contentDescription = null
            )
            Text(
                text = "${
                    when (index) {
                        0 -> rank.score
                        1 -> {
                            val time = String.format("%.2f", rank.aveTime?.toDouble()?.div(3600))
                            "$time 小时"
                        }

                        else -> {
                            val time =
                                String.format("%.2f", rank.completionTime?.toDouble()?.div(3600))
                            "$time 小时"
                        }
                    }
                }",
                modifier = Modifier.padding(end = 16.dp)
            )


        }
    }
}

@Composable
fun RankDropdownMenu(
    ranks: List<SingleTaskRank>,
    expanded: Boolean,
    expandedChanged: (Boolean) -> Unit,
    curIndex: (Int) -> Unit,
//    onDismiss: () -> Unit
) {
    var rankName by rememberSaveable {
        mutableStateOf(ranks[0].taskName)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopStart)
    ) {
        TextButton(
            onClick = { expandedChanged(!expanded) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.textButtonColors(containerColor = Color.White.copy(.7f))
        ) {
            Text(text = rankName)
            Icon(
                imageVector = if (expanded) Icons.Filled.ArrowDropDown else Icons.AutoMirrored.Filled.ArrowRight,
                contentDescription = null
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expandedChanged(false) },
            modifier = Modifier.fillMaxWidth()
        ) {
            ranks.fastForEachIndexed { index, rank ->
                DropdownMenuItem(
                    text = { Text(text = rank.taskName) },
                    onClick = {
                        expandedChanged(false)
                        curIndex(index)
                        rankName = rank.taskName
                    },
                    trailingIcon = { Text(text = "热门指数: ${rank.completionCount}") }
                )
            }

        }


    }
    /*Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopStart)
    ) {
        IconButton(onClick = { expandedChanged(true) }) {
            Icon(Icons.Default.MoreVert, contentDescription = "Localized description")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expandedChanged(false) }
        ) {
            DropdownMenuItem(
                text = { Text("Edit") },
                onClick = { *//* Handle edit! *//* },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Edit,
                        contentDescription = null
                    )
                })
            DropdownMenuItem(
                text = { Text("Settings") },
                onClick = { *//* Handle settings! *//* },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Settings,
                        contentDescription = null
                    )
                })
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text("Send Feedback") },
                onClick = { *//* Handle send feedback! *//* },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Email,
                        contentDescription = null
                    )
                },
                trailingIcon = { Text("F11", textAlign = TextAlign.Center) })
        }
    }*/


}

@Composable
fun RankDialog(user: User, onDismiss: () -> Unit) {
    AlertDialog(onDismissRequest = onDismiss, confirmButton = { /*TODO*/ }, text = {
        Column {
            Text(text = "用户信息")
            Image(
                painter = rememberAsyncImagePainter(model = user.avatarUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Text(text = "名字: ${user.userName}")
            user.academy?.let { Text(text = "学院: $it") }
            user.grade?.toString().let {
                if (it != null) {
                    Text(text = "等级: $it")
                }
            }


        }


    })
}

@Preview(showBackground = true)
@Composable
fun RankListPreview() {
    WelcomeFreshmanTheme {
//        RankScreen()
//        NewRankList()

//        OutTopRankItem()
        /*   var expanded by remember { mutableStateOf(false) }
           RankDropdownMenu(
               expanded = expanded,
               expandedChanged = { expanded = it }) { selectedRankIndex = it }*/

    }
}
