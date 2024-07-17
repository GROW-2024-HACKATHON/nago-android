package com.grow.nago.feature.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grow.nago.R
import com.grow.nago.feature.detail.DetailScreen
import com.grow.nago.ui.animation.bounceClick
import com.grow.nago.ui.component.DropShadowType
import com.grow.nago.ui.component.NagoTextField
import com.grow.nago.ui.component.dropShadow
import com.grow.nago.ui.theme.Black
import com.grow.nago.ui.theme.Gray700
import com.grow.nago.ui.theme.Orange300
import com.grow.nago.ui.theme.White
import com.grow.nago.ui.theme.body2
import com.grow.nago.ui.theme.caption2
import com.grow.nago.ui.theme.subtitle2
import com.grow.nago.ui.theme.subtitle3

@Preview
@Composable
fun HomeScreen(

) {
    var searchText by remember { mutableStateOf("") }
    var isDetailMap by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        DetailScreen()
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            NagoTextField(
                modifier = Modifier
                    .weight(1f)
                    .dropShadow(
                        type = DropShadowType.EvBlack4,
                    ),
                value = searchText,
                onValueChange = {
                    searchText = it
                },
                hint = "현미밥"
            )
            Spacer(modifier = Modifier.width(12.dp))
            DetailButton(
                text = if (isDetailMap) "목록" else "지도",
                resId = if (isDetailMap) R.drawable.ic_normal_menu else R.drawable.ic_normal_map,
                onClick = {
                    isDetailMap = isDetailMap.not()
                }
            )
        }


        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    color = White,
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp
                    )
                )
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = R.drawable.ic_user_image),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "박병준",
                    style = subtitle3,
                    color = Gray700
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(105.dp)
                    .padding(horizontal = 16.dp)
            ) {
                HomeCard(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    image = {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    color = Orange300,
                                    shape = CircleShape
                                )
                        ) {
                            Image(
                                modifier = Modifier.size(18.dp),
                                painter = painterResource(id = R.drawable.ic_normal_give),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(White)
                            )
                        }
                    },
                    title = "나눔 하기",
                    description = "음식과 행복을\n나눔하세요",
                    onClick = {
                        
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))

                HomeCard(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    image = {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    color = Orange300,
                                    shape = CircleShape
                                )
                        ) {
                            Image(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(id = R.drawable.ic_normal_speech_buble),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(White)
                            )
                        }
                    },
                    title = "채팅 하기",
                    description = "나눔을 이어서\n진행해보세요",
                    onClick = {

                    }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

        }

    }
}


@Composable
private fun HomeCard(
    modifier: Modifier = Modifier,
    image: @Composable () -> Unit,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .bounceClick(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .dropShadow(DropShadowType.EvBlack3)
                .background(
                    color = White,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            Column(
                modifier = Modifier.padding(
                    start = 8.dp,
                    top = 8.dp
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    image()
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        style = subtitle2,
                        color = Black
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    style = body2,
                    color = Gray700
                )

            }
        }
    }
}

@Composable
private fun DetailButton(
    modifier: Modifier = Modifier,
    text: String,
    @DrawableRes resId: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .bounceClick(onClick = onClick)
    ) {
        Box(
            modifier = modifier
                .size(48.dp)
                .background(
                    color = Orange300,
                    shape = RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = resId),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(White)
                )
                Text(
                    text = text,
                    style = caption2,
                    color = White
                )
            }
        }
    }
}