package com.grow.nago.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grow.nago.ui.animation.bounceClick
import com.grow.nago.ui.theme.NagoTheme
import com.grow.nago.ui.theme.Orange300
import com.grow.nago.ui.theme.White
import com.grow.nago.ui.theme.subtitle3

@Composable
fun NagoButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(12.dp),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .bounceClick(
                onClick = onClick
            )
    ) {
        Box(
            modifier = modifier
                .background(
                    color = if (enabled) Orange300 else Color(0xFFF5F6F8),
                    shape = shape
                )
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(contentPadding),
                text = text,
                color = if (enabled) White else Color(0xFFCCCCD6),
                style = subtitle3.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Preview
@Composable
private fun PreviewButton() {
    NagoTheme {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            NagoButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                text = "시작하기",
                onClick = {

                },
                contentPadding = PaddingValues(vertical = 17.5.dp)
            )
            Spacer(modifier = Modifier.height(30.dp))
            NagoButton(
                text = "시작하기",
                onClick = {

                },
                contentPadding = PaddingValues(
                    horizontal = 20.dp,
                    vertical = 12.dp
                )
            )
        }
    }
}