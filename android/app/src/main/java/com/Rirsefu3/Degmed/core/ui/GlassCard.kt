package com.Rirsefu3.Degmed.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Rirsefu3.Degmed.core.theme.SurfaceDim

@Composable
fun GlassCard(
    accent: Color,
    modifier: Modifier = Modifier,
    radius: Dp = 20.dp,
    fill: Color = SurfaceDim.copy(alpha = 0.72f),
    showBrackets: Boolean = true,
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(radius)
    Box(
        modifier = modifier
            .clip(shape)
            .background(fill)
            .border(1.dp, accent.copy(alpha = 0.35f), shape)
    ) {
        content()
        if (showBrackets) {
            HudBrackets(
                color = accent.copy(alpha = 0.55f),
                modifier = Modifier.matchParentSize(),
                armLength = 16.dp
            )
        }
    }
}
