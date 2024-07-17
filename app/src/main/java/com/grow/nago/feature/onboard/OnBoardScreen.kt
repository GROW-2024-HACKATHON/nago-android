package com.grow.nago.feature.onboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.grow.nago.R
import com.grow.nago.root.NavGroup
import com.grow.nago.ui.component.NagoButton
import com.grow.nago.ui.theme.title2

@Composable
fun OnBoardScreen(
    navController: NavController,
    navVisibleChange: (Boolean) -> Unit,
) {
    LaunchedEffect(key1 = true) {
        navVisibleChange(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            modifier = Modifier.align(Alignment.TopEnd),
            painter = painterResource(id = R.drawable.shape_round_half_orange),
            contentDescription = null
        )
        Image(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 106.dp),
            painter = painterResource(id = R.drawable.shape_pink_half),
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 68.dp, start = 34.dp),
            text = "불편했던 신고를\n이제는 간편하게",
            color = Color(0xFFFF5A36),
            style = title2
        )

        Text(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 170.dp, end = 34.dp),
            text = "사진 한장으로",
            color = Color(0xFFFF5A36),
            style = title2
        )

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            NagoButton(
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 4.dp)
                    .align(Alignment.BottomCenter),
                text = "시작하기",
                onClick = {
                    navController.navigate(NavGroup.PHONE)
                }
            )
        }
    }
}