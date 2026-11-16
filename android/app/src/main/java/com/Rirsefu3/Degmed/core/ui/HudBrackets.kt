package com.Rirsefu3.Degmed.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun HudBrackets(
    color: Color,
    modifier: Modifier = Modifier,
    armLength: Dp = 18.dp,
    strokeWidth: Dp = 1.5.dp
) {
    Canvas(modifier = modifier) {
        val arm = armLength.toPx()
        val stroke = strokeWidth.toPx()
        val w = size.width
        val h = size.height
        val inset = stroke / 2f

        drawLine(color, Offset(inset, inset), Offset(inset + arm, inset), stroke, StrokeCap.Round)
        drawLine(color, Offset(inset, inset), Offset(inset, inset + arm), stroke, StrokeCap.Round)

        drawLine(color, Offset(w - inset, inset), Offset(w - inset - arm, inset), stroke, StrokeCap.Round)
        drawLine(color, Offset(w - inset, inset), Offset(w - inset, inset + arm), stroke, StrokeCap.Round)

        drawLine(color, Offset(inset, h - inset), Offset(inset + arm, h - inset), stroke, StrokeCap.Round)
        drawLine(color, Offset(inset, h - inset), Offset(inset, h - inset - arm), stroke, StrokeCap.Round)

        drawLine(color, Offset(w - inset, h - inset), Offset(w - inset - arm, h - inset), stroke, StrokeCap.Round)
        drawLine(color, Offset(w - inset, h - inset), Offset(w - inset, h - inset - arm), stroke, StrokeCap.Round)
    }
}
