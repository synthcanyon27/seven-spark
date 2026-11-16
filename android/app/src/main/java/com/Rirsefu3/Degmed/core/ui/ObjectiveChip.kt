package com.Rirsefu3.Degmed.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.Rirsefu3.Degmed.core.theme.SurfaceRaised
import com.Rirsefu3.Degmed.core.theme.TextPrimary

@Composable
fun ObjectiveChip(
    label: String,
    progress: String,
    accent: Color,
    iconRes: Int,
    completed: Boolean,
    iconDescription: String?,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(12.dp)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .height(40.dp)
            .clip(shape)
            .background(
                if (completed) accent.copy(alpha = 0.22f) else SurfaceRaised.copy(alpha = 0.7f)
            )
            .border(1.dp, accent.copy(alpha = if (completed) 0.8f else 0.45f), shape)
            .padding(horizontal = 7.dp)
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = iconDescription,
            tint = accent,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = label,
            style = ChipTextStyle,
            color = TextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f, fill = false)
        )
        Text(
            text = progress,
            style = ChipTextStyle,
            color = accent,
            maxLines = 1
        )
    }
}

private val ChipTextStyle = TextStyle(
    fontSize = 9.sp,
    lineHeight = 12.sp,
    fontWeight = FontWeight.Bold,
    letterSpacing = 0.4.sp
)
