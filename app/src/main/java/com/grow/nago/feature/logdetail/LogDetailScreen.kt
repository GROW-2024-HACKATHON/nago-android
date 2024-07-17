package com.grow.nago.feature.logdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.grow.nago.R
import com.grow.nago.ui.animation.bounceClick
import com.grow.nago.ui.theme.Black
import com.grow.nago.ui.theme.Gray400
import com.grow.nago.ui.theme.Gray700
import com.grow.nago.ui.theme.Gray800
import com.grow.nago.ui.theme.Orange200
import com.grow.nago.ui.theme.Red300
import com.grow.nago.ui.theme.subtitle1
import com.grow.nago.ui.theme.subtitle3

@Composable
fun LogDetailScreen(
    viewModel: LogDetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    id: Int,
    navController: NavController,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val item = state.reportData

    LaunchedEffect(key1 = true) {
        viewModel.load(id)
    }
    Column {
        Box(
            modifier = Modifier
                .size(44.dp)
                .bounceClick(
                    onClick = {
                        navController.popBackStack()
                    }
                )
        ) {
            Image(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Center),
                painter = painterResource(id = R.drawable.ic_normal_arrow_left),
                contentDescription = null
            )
        }
        Text(
            modifier = Modifier.padding(
                start = 16.dp
            ),
            text = state.reportData.title,
            style = subtitle1,
            color = Black
        )
        Spacer(modifier = Modifier.height(12.dp))
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            thickness = 1.dp,
            color = Gray400
        )

        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.padding(
                start = 16.dp
            )
        ) {
            Text(
                text = "상태 :",
                style = subtitle3,
                color = Gray700
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = item.type?: "",
                style = subtitle3,
                color = when(item.type) {
                    "처리중" -> Red300
                    "진행전" -> Orange200
                    else -> Color(0xFF41A7F1)
                }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.padding(
                start = 16.dp
            )
        ) {
            Text(
                text = "신고일 :",
                style = subtitle3,
                color = Gray700
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${item.createdAt[0]}년 ${item.createdAt[1]}월 ${item.createdAt[2]}일 ${item.createdAt[3]}시 ${item.createdAt[4]}분",
                style = subtitle3,
                color = Gray700
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.padding(
                start = 16.dp
            )
        ) {
            Text(
                text = "카테고리 :",
                style = subtitle3,
                color = Gray700
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = item.small,
                style = subtitle3,
                color = Gray700
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            thickness = 1.dp,
            color = Gray400
        )


        Spacer(modifier = Modifier.height(12.dp))
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "내용",
            style = subtitle3,
            color = Gray800
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = item.content,
            style = subtitle3,
            color = Gray700
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "이미지",
            style = subtitle3,
            color = Gray800
        )
        Spacer(modifier = Modifier.height(4.dp))
        Image(
            modifier = Modifier.padding(horizontal = 16.dp),
            painter = rememberAsyncImagePainter(model = item.firstImage),
            contentDescription = null
        )

    }
}