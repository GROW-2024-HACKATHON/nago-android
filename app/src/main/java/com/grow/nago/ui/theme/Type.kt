package com.grow.nago.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.grow.nago.R

val PretendardFontFamily = FontFamily(
    Font(R.font.pretendard_regular, FontWeight.Normal),
    Font(R.font.pretendard_bold, FontWeight.Bold),
    Font(R.font.pretendard_semi_bold, FontWeight.SemiBold),
)

val caption1 = TextStyle(
    fontFamily = PretendardFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 12.sp,
    lineHeight = 1.3.em,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
)

val caption2 = TextStyle(
    fontFamily = PretendardFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    lineHeight = 1.3.em,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
)

val body1 = TextStyle(
    fontFamily = PretendardFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 14.sp,
    lineHeight = 1.3.em,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
)

val body2 = TextStyle(
    fontFamily = PretendardFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    lineHeight = 1.3.em,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
)

val subtitle1 = TextStyle(
    fontFamily = PretendardFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 20.sp,
    lineHeight = 1.3.em,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
)

val subtitle2 = TextStyle(
    fontFamily = PretendardFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 18.sp,
    lineHeight = 1.3.em,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
)

val subtitle3 = TextStyle(
    fontFamily = PretendardFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 16.sp,
    lineHeight = 1.3.em,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
)

val title1 = TextStyle(
    fontFamily = PretendardFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 28.sp,
    lineHeight = 1.3.em,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
)

val title2 = TextStyle(
    fontFamily = PretendardFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 24.sp,
    lineHeight = 1.3.em,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
)

val display1 = TextStyle(
    fontFamily = PretendardFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 36.sp,
    lineHeight = 1.3.em,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
)

val display2 = TextStyle(
    fontFamily = PretendardFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 32.sp,
    lineHeight = 1.3.em,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)