package com.Rirsefu3.Degmed.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.Rirsefu3.Degmed.core.theme.AccentBrass
import com.Rirsefu3.Degmed.core.theme.TextSecondary

@Composable
fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier,
    accent: Color = AccentBrass
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(accent.copy(alpha = 0.4f))
        )
    }
}
