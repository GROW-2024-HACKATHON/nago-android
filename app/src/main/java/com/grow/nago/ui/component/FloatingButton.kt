package com.grow.nago.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import com.grow.nago.ui.animation.bounceClick
import com.grow.nago.ui.theme.Orange300
import com.grow.nago.ui.theme.White

@Composable
fun FloatingButton(
    modifier: Modifier,
    @DrawableRes resId: Int,
    shape: Shape,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .bounceClick(onClick = onClick)
    ) {
        Box(
            modifier = modifier
                .background(
                    color = Orange300,
                    shape = shape
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = resId),
                contentDescription = null,
                colorFilter = ColorFilter.tint(White)
            )
        }
    }
}