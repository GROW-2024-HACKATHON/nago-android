package com.grow.nago.feature.log

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.grow.nago.R
import com.grow.nago.ui.animation.bounceClick
import com.grow.nago.ui.component.DropShadowType
import com.grow.nago.ui.component.dropShadow
import com.grow.nago.ui.theme.Black
import com.grow.nago.ui.theme.Gray600
import com.grow.nago.ui.theme.White
import com.grow.nago.ui.theme.body2
import com.grow.nago.ui.theme.subtitle1
import com.grow.nago.ui.theme.subtitle3

@Preview
@Composable
fun LogScreen(
//    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            modifier = Modifier.padding(
                start = 20.dp
            ),
            text = "최근 신고한 내역이에요",
            style = subtitle1,
            color = Black
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(5) {
                Column {
                    LogCard(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        title = "불법 주정차를 신고합니다.",
                        date = "2024.07.17",
                        category = "불법 주정차",
                        onClick = {}
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun LogCard(
    modifier: Modifier = Modifier,
    title: String,
    date: String,
    category: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.bounceClick(onClick)
    ) {
        Row(
            modifier = modifier
                .height(74.dp)
                .dropShadow(DropShadowType.EvBlack2)
                .background(
                    color = White,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                modifier = Modifier
                    .size(42.dp)
                    .align(Alignment.CenterVertically),
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = title,
                    style = subtitle3,
                    color = Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Text(
                        text = date,
                        style = body2,
                        color = Gray600
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "·",
                        style = body2,
                        color = Gray600
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = category,
                        style = body2,
                        color = Gray600
                    )
                }
            }
            Image(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .size(24.dp),
                painter = painterResource(id = R.drawable.ic_normal_detail_vertical),
                contentDescription = null
            )
        }
    }
}