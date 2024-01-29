package com.example.welcome_freshman.feature.main.list

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.welcome_freshman.R
import com.example.welcome_freshman.feature.main.profile.CommonDivider
import kotlinx.coroutines.launch

/**
 *@date 2024/1/27 10:49
 *@author GFCoder
 */

@Composable
fun ListRoute(onDetailClick: () -> Unit) {
    ListScreen(onDetailClick = onDetailClick)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListScreen(onDetailClick: () -> Unit) {
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
            contentPadding = PaddingValues(top = 8.dp),
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
                for (i in 1 until 51) {
                    if (i == 1) {
                        item { RankItem(rank = i.toString(), rankIcon = R.drawable.rank_first) }
                    }
                    if (i == 2) {
                        item {
                            Divider(
                                Modifier.padding(vertical = 3.dp),
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.primaryContainer
                            )
                        }
                        item { RankItem(rank = i.toString(), rankIcon = R.drawable.rank_second) }
                    }
                    if (i == 3) {
                        item {
                            Divider(
                                Modifier.padding(vertical = 5.dp),
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.primaryContainer
                            )
                        }
                        item { RankItem(rank = i.toString(), rankIcon = R.drawable.rank_third) }
                    }
                    if (i > 3) {
                        item {
                            Divider(
                                Modifier.padding(vertical = 3.dp),
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.primaryContainer
                            )
                        }
                        item { RankItem(rank = i.toString()) }

                    }
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
    @DrawableRes rankIcon: Int? = null
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp), Arrangement.SpaceBetween
    ) {
        Text(text = nickName)
        if (rankIcon != null) {
            Image(painter = painterResource(id = rankIcon), contentDescription = null)
        } else {
            Text(text = rank, Modifier.padding(end = 5.dp))
        }
    }
}

data class Rank(
    val nickName: String
)

