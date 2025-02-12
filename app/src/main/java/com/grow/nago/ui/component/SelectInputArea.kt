package com.grow.nago.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.grow.nago.R
import com.grow.nago.ui.theme.Black
import com.grow.nago.ui.theme.NagoTheme
import com.grow.nago.ui.theme.White
import com.grow.nago.ui.theme.body2
import com.grow.nago.ui.theme.subtitle3




@Composable
fun NagoButtonSelectMenu(
    itemList: List<String>,
    text: String = "",
    modifier: Modifier = Modifier,
    hint: String = "",
    onSelectItemListener: (String) -> Unit
) {
    // 1. DropDownMenu의 펼쳐짐 상태 정의
    var isDropDownMenuExpanded by remember { mutableStateOf(false) }
    var buttonSize by remember { mutableStateOf(Size.Zero) }
    val focusRequester = remember { FocusRequester() }
    Column {
        Button(
            modifier = modifier
                .onGloballyPositioned { coordinates ->
                    buttonSize = coordinates.size.toSize()
                }
                .focusRequester(focusRequester),
            colors = ButtonDefaults.buttonColors(containerColor = White, contentColor = Black),
            shape = RoundedCornerShape(15.dp),
            border = BorderStroke(1.dp, color = Color(0xFFCCCCD6)),
            onClick = { isDropDownMenuExpanded = true }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = (-10).dp)
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(
                            start = 0.dp
                        ),
                    text = text,
                    color = Black,
                    style = subtitle3
                )
                Image(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(
                            end = 0.dp
                        ),
                    painter = painterResource(id = R.drawable.ic_normal_expand_down),
                    contentDescription = "아래 화살표"
                )
            }

        }

        // 3. DropDownMenu 정의
        DropdownMenu(
            modifier = Modifier
                .width(
                    with(LocalDensity.current) {
                        buttonSize.width.toDp()
                    }
                )
                .heightIn(max = 200.dp)
                .background(White),
            expanded = isDropDownMenuExpanded,
            onDismissRequest = { isDropDownMenuExpanded = false }
        ) {
            itemList.forEachIndexed { index, label ->
                DropdownMenuItem(
                    modifier = Modifier.background(White),
                    text = {
                        Text(
                            text = label,
                            color = Black,
                            style = body2
                        )
                    },
                    onClick = {
                        onSelectItemListener(label)
                        isDropDownMenuExpanded = false
                    }
                )
            }
        }
    }

}

@Preview
@Composable
private fun Preview() {
    NagoTheme {
        NagoButtonSelectMenu(
            listOf("test", "test1", "test2"),
            "",
            Modifier.height(48.dp),
            "",
            {}
        )
    }
}


