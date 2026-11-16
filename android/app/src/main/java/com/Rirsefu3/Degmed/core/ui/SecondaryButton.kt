package com.Rirsefu3.Degmed.core.ui

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.BorderStroke
import com.Rirsefu3.Degmed.core.theme.AccentCyan
import com.Rirsefu3.Degmed.core.theme.DisabledSurface
import com.Rirsefu3.Degmed.core.theme.SurfaceRaised
import com.Rirsefu3.Degmed.core.theme.TextMuted

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    accent: Color = AccentCyan,
    height: Dp = 52.dp
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(
            width = 1.2.dp,
            color = if (enabled) accent.copy(alpha = 0.7f) else DisabledSurface
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = SurfaceRaised.copy(alpha = 0.6f),
            contentColor = accent,
            disabledContainerColor = DisabledSurface.copy(alpha = 0.5f),
            disabledContentColor = TextMuted
        ),
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = height)
            .height(height)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}
