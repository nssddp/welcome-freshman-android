package com.example.welcome_freshman.feature.main.list

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.welcome_freshman.R
import com.example.welcome_freshman.feature.main.profile.CommonDivider
import com.example.welcome_freshman.ui.theme.WelcomeFreshmanTheme
import kotlinx.coroutines.launch

/**
 *@date 2024/1/27 10:49
 *@author GFCoder
 */

@Composable
fun ListRoute() {
    ListScreen()
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListScreen() {
    val tabList = listOf("排行榜1", "排行榜2", "排行榜3")
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState {
        tabList.size
    }
    val selectedTabIndex by remember {
        derivedStateOf { pagerState.currentPage }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabList.forEachIndexed { index, currentTab ->
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
            contentPadding = PaddingValues(top = 0.dp),
            modifier = Modifier
                .fillMaxSize()
        ) {
            /*Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column {
                    Text(text = "This is ${tabList[selectedTabIndex]}")
                    Button(onClick = onDetailClick) {
                        Text(text = "go to detail")
                    }
                }

            }*/

            LazyColumn(Modifier.fillMaxSize()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding( bottom = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        OutRankItem(
                            modifier = Modifier
                                .padding(top = 26.dp, end = 168.dp),
                            "无名氏",
                            rankIcon = R.drawable.rank_second
                        )
                        OutRankItem(
                            modifier = Modifier
                                .padding(top = 26.dp, start = 168.dp),
                            "无名氏",
                            rankIcon = R.drawable.rank_third
                        )
                        OutRankItem(
                            nickName = "无名氏",
                            rankIcon = R.drawable.rank_first
                        )
                    }
                }

                for (i in 4 until 51) {
                    item {
                        Divider(
                            Modifier.padding(vertical = 3.dp),
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.primaryContainer
                        )
                    }
                    item { RankItem(rank = i.toString()) }


                }
                item { CommonDivider() }
            }


        }


    }
}

@Composable
fun RankItem(
    nickName: String = "无名氏",
    rank: String,
//    @DrawableRes rankIcon: Int? = null
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .padding(top = 6.dp, bottom = 6.dp),
        Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = rank, Modifier.padding(end = 5.dp))

        Text(text = nickName, style = MaterialTheme.typography.bodyMedium)
        /*if (rankIcon != null) {
            Image(painter = painterResource(id = rankIcon), contentDescription = null)
        } */
    }
}

@Composable
fun OutRankItem(modifier: Modifier = Modifier, nickName: String, @DrawableRes rankIcon: Int) {
    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.medium
            )
            .size(95.dp, 145.dp),
    ) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp
            )
        ) {
            Column(
                Modifier
                    .padding(top = 6.dp)
                    .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = R.drawable.logo, contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {}
                )
                Text(text = nickName, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(12.dp))
                Image(painter = painterResource(rankIcon), contentDescription = null)

            }
        }


    }
}

data class Rank(
    val nickName: String
)


@Preview(showBackground = true)
@Composable
fun RankList() {
    WelcomeFreshmanTheme {
        ListScreen()
    }
}
