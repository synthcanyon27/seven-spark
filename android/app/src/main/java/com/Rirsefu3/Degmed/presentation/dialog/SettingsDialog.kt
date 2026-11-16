package com.Rirsefu3.Degmed.presentation.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.Rirsefu3.Degmed.R
import com.Rirsefu3.Degmed.core.theme.AccentBrass
import com.Rirsefu3.Degmed.core.theme.AccentCyan
import com.Rirsefu3.Degmed.core.theme.AccentViolet
import com.Rirsefu3.Degmed.core.theme.DangerDrift
import com.Rirsefu3.Degmed.core.theme.DisabledSurface
import com.Rirsefu3.Degmed.core.theme.SurfaceDim
import com.Rirsefu3.Degmed.core.theme.TextPrimary
import com.Rirsefu3.Degmed.core.theme.TextSecondary
import com.Rirsefu3.Degmed.domain.model.GameSettings

@Composable
fun SettingsDialog(
    settings: GameSettings,
    onNumerals: (Boolean) -> Unit,
    onBandLabels: (Boolean) -> Unit,
    onParticles: (Boolean) -> Unit,
    onRequestReset: () -> Unit,
    onClose: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onClose,
        containerColor = SurfaceDim,
        shape = RoundedCornerShape(24.dp),
        title = {
            Text(
                text = stringResource(R.string.settings),
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                SettingsToggle(
                    label = stringResource(R.string.settings_numerals),
                    checked = settings.numeralsOnTiles,
                    accent = AccentCyan,
                    onChange = onNumerals
                )
                SettingsToggle(
                    label = stringResource(R.string.settings_band_labels),
                    checked = settings.bandLabels,
                    accent = AccentBrass,
                    onChange = onBandLabels
                )
                SettingsToggle(
                    label = stringResource(R.string.settings_particles),
                    checked = settings.particleTrails,
                    accent = AccentViolet,
                    onChange = onParticles
                )
                TextButton(
                    onClick = onRequestReset,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp)
                ) {
                    Text(
                        text = stringResource(R.string.reset_progress),
                        style = MaterialTheme.typography.labelLarge,
                        color = DangerDrift
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onClose, modifier = Modifier.heightIn(min = 48.dp)) {
                Text(
                    text = stringResource(R.string.close),
                    style = MaterialTheme.typography.labelLarge,
                    color = AccentCyan
                )
            }
        }
    )
}

@Composable
private fun SettingsToggle(
    label: String,
    checked: Boolean,
    accent: androidx.compose.ui.graphics.Color,
    onChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
        Switch(
            checked = checked,
            onCheckedChange = onChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = TextPrimary,
                checkedTrackColor = accent,
                uncheckedThumbColor = TextSecondary,
                uncheckedTrackColor = DisabledSurface
            ),
            modifier = Modifier.semantics {
                stateDescription = label
            }
        )
    }
}

@Composable
fun ResetConfirmDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        containerColor = SurfaceDim,
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(
                text = stringResource(R.string.reset_progress),
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
        },
        text = {
            Text(
                text = stringResource(R.string.settings_reset_confirm),
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm, modifier = Modifier.heightIn(min = 48.dp)) {
                Text(
                    text = stringResource(R.string.reset),
                    style = MaterialTheme.typography.labelLarge,
                    color = DangerDrift
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel, modifier = Modifier.heightIn(min = 48.dp)) {
                Text(
                    text = stringResource(R.string.cancel),
                    style = MaterialTheme.typography.labelLarge,
                    color = AccentCyan
                )
            }
        }
    )
}
