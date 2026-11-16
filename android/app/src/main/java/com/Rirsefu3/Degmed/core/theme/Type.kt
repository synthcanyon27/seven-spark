package com.Rirsefu3.Degmed.core.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val AppTypography = Typography(
    displaySmall = TextStyle(
        fontWeight = FontWeight.ExtraBold,
        fontSize = 40.sp,
        lineHeight = 46.sp,
        letterSpacing = 5.sp
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        lineHeight = 32.sp,
        letterSpacing = 2.sp
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.5.sp
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.2.sp
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.ExtraBold,
        fontSize = 18.sp,
        lineHeight = 22.sp,
        letterSpacing = 2.sp
    ),
    labelSmall = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        lineHeight = 15.sp,
        letterSpacing = 1.6.sp
    )
)

val NumeralStyle = TextStyle(
    fontFamily = FontFamily.Monospace,
    fontWeight = FontWeight.ExtraBold,
    fontSize = 20.sp,
    lineHeight = 22.sp,
    fontFeatureSettings = "tnum"
)

val ReadoutStyle = TextStyle(
    fontFamily = FontFamily.Monospace,
    fontWeight = FontWeight.ExtraBold,
    fontSize = 22.sp,
    lineHeight = 26.sp,
    fontFeatureSettings = "tnum"
)
