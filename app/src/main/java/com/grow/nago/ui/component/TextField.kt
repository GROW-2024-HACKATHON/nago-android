package com.grow.nago.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grow.nago.R
import com.grow.nago.ui.theme.Black
import com.grow.nago.ui.theme.NagoTheme
import com.grow.nago.ui.theme.Orange300
import com.grow.nago.ui.theme.Transparent
import com.grow.nago.ui.theme.White
import com.grow.nago.ui.theme.subtitle3

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NagoTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    hint: String = "",
    includeIcon: Boolean = true,
    enabled: Boolean = true,
    secured: Boolean = false,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    textStyle: TextStyle = subtitle3.copy(fontWeight = FontWeight.Normal),
    shape: Shape = RoundedCornerShape(12.dp),
    trailingIcon: @Composable (() -> Unit)? = null,
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedTextColor = Black,
        focusedContainerColor = Transparent,
        focusedIndicatorColor = Transparent,
        unfocusedContainerColor = Transparent,
        unfocusedTextColor = Black,
        unfocusedIndicatorColor = Transparent,
        disabledIndicatorColor = Transparent,
        disabledTextColor = Color(0xFFE3E3E9),
        disabledContainerColor = Color.Transparent,
    ),
) {
    var isFocused by remember { mutableStateOf(false) }
    var showText by remember { mutableStateOf(false) }
    val animBorderColor by animateColorAsState(
        targetValue = if (isFocused) Orange300 else Color(0xFFCCCCD6),
        label = "",
    )
    val icon = if (!secured) {
        R.drawable.ic_normal_close_fill
    } else if (showText) {
        R.drawable.ic_normal_show
    } else {
        R.drawable.ic_normal_hide
    }

    val isSecured = secured && !showText

    BasicTextField(
        value = value,
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = White,
                shape = shape,
            )
            .onFocusChanged {
                isFocused = it.isFocused
            }
            .border(
                width = 1.dp,
                color = animBorderColor,
                shape = shape,
            ),
        onValueChange = onValueChange,
        enabled = enabled,
        textStyle = textStyle.copy(color = Black),
        singleLine = singleLine,
        keyboardOptions = if (isSecured) KeyboardOptions(keyboardType = KeyboardType.Password) else KeyboardOptions.Default,
        visualTransformation = if (isSecured) PasswordVisualTransformation() else VisualTransformation.None,
        cursorBrush = SolidColor(Orange300),
        decorationBox = @Composable { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = value,
                innerTextField = innerTextField,
                placeholder = {
                    Text(
                        text = hint,
                        style = textStyle,
                        color = if (enabled) Color(0xFFCCCCD6) else Color(0xFFE3E3E9),
                    )
                },
                label = null,
                trailingIcon = {
                    if (trailingIcon != null) {
                        trailingIcon()
                    } else if (value.isNotEmpty() && includeIcon) {
                        Image(
                            modifier = Modifier
                                .size(24.dp)
                                .clickable(onClick = {
                                    if (!secured) {
                                        onValueChange("")
                                    } else {
                                        showText = !showText
                                    }
                                }),
                            painter = painterResource(id = icon),
                            colorFilter = ColorFilter.tint(Color(0xFFCCCCD6)),
                            contentDescription = null
                        )
                    }
                },
                contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(
                    start = 12.dp,
                    end = 8.dp,
                    top = 14.dp,
                    bottom = 14.dp
                ),
                shape = shape,
                enabled = enabled,
                colors = colors,
                interactionSource = remember { MutableInteractionSource() },
                singleLine = false,
                visualTransformation = VisualTransformation.None,
            )
        },
    )
}

@Composable
@Preview
private fun Preview() {
    NagoTheme {
        Column(
            modifier = Modifier
                .background(White)
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            var value by remember { mutableStateOf("야삐") }
            NagoTextField(
                value = value,
                hint = "야삐",
                onValueChange = {
                    value = it
                }
            )
            NagoTextField(
                value = value,
                hint = "야삐",
                onValueChange = {
                    value = it
                },
                enabled = false,
            )
            NagoTextField(
                value = value,
                hint = "야삐",
                onValueChange = {
                    value = it
                },
                secured = true,
                enabled = false,
            )
        }
    }
}
