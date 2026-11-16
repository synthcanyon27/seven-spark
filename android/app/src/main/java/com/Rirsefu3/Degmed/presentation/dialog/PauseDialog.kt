package com.Rirsefu3.Degmed.presentation.dialog

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Rirsefu3.Degmed.R
import com.Rirsefu3.Degmed.core.theme.AccentCyan
import com.Rirsefu3.Degmed.core.theme.SurfaceDim
import com.Rirsefu3.Degmed.core.theme.TextPrimary
import com.Rirsefu3.Degmed.core.theme.TextSecondary

@Composable
fun PauseDialog(
    levelTitle: String,
    movesLeft: Int,
    onResume: () -> Unit,
    onMenu: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onResume,
        containerColor = SurfaceDim,
        shape = RoundedCornerShape(24.dp),
        title = {
            Text(
                text = stringResource(R.string.paused),
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary
            )
        },
        text = {
            Text(
                text = stringResource(R.string.paused_body, levelTitle, movesLeft),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
        },
        confirmButton = {
            TextButton(onClick = onResume, modifier = Modifier.heightIn(min = 48.dp)) {
                Text(
                    text = stringResource(R.string.resume),
                    style = MaterialTheme.typography.labelLarge,
                    color = AccentCyan
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onMenu, modifier = Modifier.heightIn(min = 48.dp)) {
                Text(
                    text = stringResource(R.string.menu),
                    style = MaterialTheme.typography.labelLarge,
                    color = TextSecondary
                )
            }
        }
    )
}
