package com.example.welcome_freshman.feature.pointShop

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.welcome_freshman.R

/**
 *@date 2024/4/14 21:31
 *@author GFCoder
 */

@Composable
fun ShopRoute(onBackClick: () -> Unit) {

    val goodsList = listOf(
        Goods(
            "https://img.alicdn.com/imgextra/i3/120909242/O1CN01N8I8UU2I8tJ0mvawJ_!!0-saturn_solar.jpg_360x360xzq75.jpg_.webp",
            "https://img.alicdn.com/imgextra/i1/3021978030/O1CN0172SW6j29BnGW7MImi_!!3021978030.jpg",
            100,
            "陶瓷咖啡水杯",
            "500"
        ),
        Goods(
            "https://img.alicdn.com/imgextra/i1/192470019/O1CN01ybygs41C0kPsBVReB_!!0-saturn_solar.jpg_360x360xzq75.jpg_.webp",
            "https://img.alicdn.com/imgextra/i4/3538027462/O1CN017coQOZ24zeMkM3D0T_!!3538027462.jpg",
            350,
            "陶瓷内胆保温杯",
            "800"
        ),
        Goods(
            "https://img.alicdn.com/imgextra/i3/2139080081/O1CN01NsGDTy1CT8vJYuSNY_!!0-saturn_solar.jpg_360x360xzq75.jpg_.webp",
            "https://img.alicdn.com/imgextra/i2/2212778784135/O1CN014HBPTC1gPsgDC7yxc_!!2212778784135.jpg",
            200,
            "山海蓝马克杯",
            "650"
        ),
        Goods(
            "https://img.alicdn.com/imgextra/i4/542280092/O1CN014zi4Jq1CYBNYDdKxE_!!0-saturn_solar.jpg_360x360xzq75.jpg_.webp",
            "https://img.alicdn.com/imgextra/i2/2206538422980/O1CN012MphsY1Xst2FKpgVW_!!2206538422980.jpg",
            120,
            "无线蓝牙耳机头戴式",
            "700"
        ),
        Goods(
            "https://img.alicdn.com/imgextra/i4/32990455/O1CN018bCXUx1FER5X4XkiJ_!!0-saturn_solar.jpg_360x360xzq75.jpg_.webp",
            "https://img.alicdn.com/imgextra/i4/1039081494/O1CN01E6UyHb1MuIbEpXaVQ_!!1039081494.jpg",
            180,
            "开放式蓝牙耳机",
            "450"
        ),
        Goods(
            "https://img.alicdn.com/imgextra/i3/97346331/O1CN01WCxyNT1wdePZsceZb_!!0-saturn_solar.jpg_360x360xzq75.jpg_.webp",
            "https://img.alicdn.com/imgextra/i3/2229687997/O1CN01bvV7A728wgE3zbG4p_!!2229687997.jpg",
            200,
            "民族风斜挎包",
            "300"
        ),
        Goods(
            "https://img.alicdn.com/imgextra/i1/2207976873339/O1CN01cp9mhe1aXJPSvLtbc_!!2207976873339-0-alimamacc.jpg_360x360xzq75.jpg_.webp",
            "https://img.alicdn.com/imgextra/i1/2207976873339/O1CN01NjoUID1aXJOSaBHPt_!!2207976873339.jpg",
            140,
            "手提袋定制",
            "900"
        ),
        Goods(
            "https://img.alicdn.com/imgextra/i1/116408869/O1CN01s1lFe82FO3edcCpWB_!!0-saturn_solar.jpg_360x360xzq75.jpg_.webp",
            "//gw.alicdn.com/imgextra/i2/559208837/O1CN01IJA5mm2F9OvGwstTs_!!559208837.jpg_Q75.jpg_.webp",
            220,
            "EVA手机壳",
            "1200"
        ),
        Goods(
            "https://g-search1.alicdn.com/img/bao/uploaded/i4/i2/2598622252/O1CN01YbUDSo1SVSsavDHbk_!!2598622252.jpg_360x360q90.jpg_.webp",
            "//gw.alicdn.com/imgextra/i1/2598622252/O1CN01B8OPBH1SVSss30VJt_!!2598622252.jpg_Q75.jpg_.webp",
            70,
            "小马宝莉立牌",
            "250"
        ),
        Goods(
            "https://g-search3.alicdn.com/img/bao/uploaded/i4/i4/2214422334112/O1CN01sMsbx21gFLTGiFIv7_!!0-item_pic.jpg_360x360q90.jpg_.webp",
            "https://img.alicdn.com/imgextra/i1/2214422334112/O1CN01AwH1oo1gFLWCKAySb_!!2214422334112.jpg",
            160,
            "竹节高升手链",
            "1000"
        ),
//        Goods("https://example.com/img11.jpg", 110, "Shop K", "550"),
//        Goods("https://example.com/img12.jpg", 190, "Shop L", "850"),
//        Goods("https://example.com/img13.jpg", 240, "Shop M", "1300"),
//        Goods("https://example.com/img14.jpg", 80, "Shop N", "350"),
//        Goods("https://example.com/img15.jpg", 130, "Shop O", "600"),
//        Goods("https://example.com/img16.jpg", 200, "Shop P", "1100"),
//        Goods("https://example.com/img17.jpg", 100, "Shop Q", "400"),
//        Goods("https://example.com/img18.jpg", 170, "Shop R", "950"),
//        Goods("https://example.com/img19.jpg", 230, "Shop S", "1400"),
//        Goods("https://example.com/img20.jpg", 60, "Shop T", "200")
    )

    PointShopScreen(goodsList = goodsList, onBackClick = onBackClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PointShopScreen(goodsList: List<Goods>, onBackClick: () -> Unit) {
    var goodsName by remember {
        mutableStateOf("积分商城")
    }

    var showGoodsDetail by remember {
        mutableStateOf(false)
    }
    BackHandler(enabled = showGoodsDetail) {
        showGoodsDetail = false
        goodsName = "积分商城"
    }

    var goods by remember {
        mutableStateOf(goodsList[0])
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = goodsName,
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (showGoodsDetail) {
                            showGoodsDetail = false
                            goodsName = "积分商城"
                        } else onBackClick()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier.shadow(3.dp)
            )
        }
    ) {
        Column(
            Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            AnimatedContent(targetState = showGoodsDetail, label = "") { targetDetailVisible ->
                if (targetDetailVisible) {
                    GoodsDetail(goods)
                } else {
                    GoodsList(goodsList, goods) { selectedGoods ->
                        goodsName = selectedGoods.goodsName
                        goods = selectedGoods
                        showGoodsDetail = true
                    }
                }
            }
        }
    }
}

@Composable
fun GoodsList(goodsList: List<Goods>, currentGoods: Goods, onGoodsSelected: (Goods) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 20.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        itemsIndexed(goodsList) { index, it ->
            ShopItem(goods = it, onClick = {
                onGoodsSelected(it)
            })
        }
    }
}


@Composable
fun ShopItem(goods: Goods, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .size(width = 114.dp, height = 240.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 3.dp
    ) {
        Column(
            Modifier
                .fillMaxSize()
        ) {
            AsyncImage(
                model = goods.imgUrl, contentDescription = null, contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .clipToBounds()
                    .weight(1f)
            )
            Text(
                text = goods.goodsName, modifier = Modifier
                    .padding(vertical = 6.dp, horizontal = 4.dp)
            )

            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.point),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 4.dp)
//                        .padding(horizontal = 4.dp)
                        .size(16.dp)

                )

                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "${goods.value}", fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${goods.exchangeNum}人兑换",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(
                        0xCD474747
                    ),
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }

        }
    }
}


@Composable
fun GoodsDetail(goods: Goods) {
    LazyColumn(Modifier.fillMaxSize()) {
        item {
            Card(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = goods.imgUrl),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clipToBounds()
                )
                Text(
                    text = goods.goodsName,
                    Modifier.padding(start = 10.dp, top = 10.dp, bottom = 30.dp),
                    fontWeight = FontWeight.Bold,

                    )
            }
        }
        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 40.dp, bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = goods.value.toString(), fontSize = 25.sp)
                Spacer(modifier = Modifier.width(3.dp))
                Text(text = "积分")
            }
            ElevatedButton(
                onClick = { /*TODO*/ },
                elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(text = "立即兑换")
            }
        }

        item {
            Spacer(
                modifier = Modifier
                    .height(20.dp)
            )
            Text(
                text = "商品详情",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(start = 20.dp)
            )
            Image(
                painter = rememberAsyncImagePainter(model = goods.detailImgUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp)
                    .height(500.dp),
                contentScale = ContentScale.FillHeight
            )
        }

    }
}


data class Goods(
    val imgUrl: String,
    val detailImgUrl: String,
    val value: Int,
    val goodsName: String,
    val exchangeNum: String,
)

@Preview(showBackground = true)
@Composable
private fun ShopScreenPreview() {

//    val goodsList =

//    PointShopScreen(goodsList, onBackClick = {})
//    GoodsDetail(goodsList[0])
//    ShopItem()
}