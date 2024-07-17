package com.grow.nago.feature.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.grow.nago.root.NavGroup
import com.grow.nago.ui.animation.bounceClick
import com.grow.nago.ui.theme.Black
import com.grow.nago.ui.theme.Gray700
import com.grow.nago.ui.theme.Red300
import com.grow.nago.ui.theme.White
import com.grow.nago.ui.theme.subtitle1
import com.grow.nago.ui.theme.subtitle3

@Composable
fun SettingScreen(
    viewModel: SettingViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navController: NavController
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
            text = "설정",
            style = subtitle1,
            color = Black
        )
        Spacer(modifier = Modifier.height(16.dp))
        SettingCard(
            title = "로그아웃",
            textColor = Red300,
            onClick = {
                viewModel.removeData()
                while (navController.popBackStack()) { }
                navController.navigate(NavGroup.ONBOARD)
            }
        )
    }
}

@Composable
private fun SettingCard(
    title: String,
    textColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .bounceClick(
                onClick = onClick
            )
    ) {
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier
                .padding(
                    vertical = 12.dp
                )
                .padding(
                    start = 8.dp
                ),
            text = title,
            style = subtitle3,
            color = textColor
        )
    }
}