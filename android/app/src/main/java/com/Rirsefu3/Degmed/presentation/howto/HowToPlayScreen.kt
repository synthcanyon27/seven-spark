package com.Rirsefu3.Degmed.presentation.howto

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Rirsefu3.Degmed.R
import com.Rirsefu3.Degmed.core.theme.AccentBrass
import com.Rirsefu3.Degmed.core.theme.AccentCyan
import com.Rirsefu3.Degmed.core.theme.AccentViolet
import com.Rirsefu3.Degmed.core.theme.BandJade
import com.Rirsefu3.Degmed.core.theme.TextPrimary
import com.Rirsefu3.Degmed.core.theme.TextSecondary
import com.Rirsefu3.Degmed.core.ui.GlassCard
import com.Rirsefu3.Degmed.core.ui.ScreenBackground
import com.Rirsefu3.Degmed.core.ui.SecondaryButton
import com.Rirsefu3.Degmed.core.ui.SectionTitle

@Composable
fun HowToPlayScreen(
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    ScreenBackground(
        topColor = Color(0xF20A0014),
        midColor = Color(0xE6120026),
        bottomColor = Color(0xF20A0014),
        particleDensity = 0,
        particlesEnabled = false,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
            ) {
                TextButton(onClick = onMenu, modifier = Modifier.height(48.dp)) {
                    Image(
                        painter = painterResource(R.drawable.icon_home),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(
                        text = stringResource(R.string.menu),
                        style = MaterialTheme.typography.labelSmall,
                        color = AccentCyan
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(R.string.how_to_play),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
            ) {
                InfoBlock(
                    title = stringResource(R.string.howto_controls),
                    accent = AccentCyan,
                    lines = listOf(
                        R.drawable.ic_spin to stringResource(R.string.howto_controls_1),
                        R.drawable.ic_spin to stringResource(R.string.howto_controls_2),
                        R.drawable.ic_freeze to stringResource(R.string.howto_controls_3),
                        R.drawable.ic_rewind to stringResource(R.string.howto_controls_4)
                    )
                )
                InfoBlock(
                    title = stringResource(R.string.howto_objectives),
                    accent = AccentBrass,
                    lines = listOf(
                        R.drawable.ic_sum to stringResource(R.string.howto_objectives_1),
                        R.drawable.ic_triple to stringResource(R.string.howto_objectives_2),
                        R.drawable.ic_spectrum to stringResource(R.string.howto_objectives_3)
                    )
                )
                ModifierBlock()
                InfoBlock(
                    title = stringResource(R.string.howto_drift),
                    accent = AccentViolet,
                    lines = listOf(
                        R.drawable.ic_spin to stringResource(R.string.howto_drift_1),
                        R.drawable.ic_spectrum to stringResource(R.string.howto_drift_2),
                        R.drawable.ic_check to stringResource(R.string.howto_drift_3)
                    )
                )
                InfoBlock(
                    title = stringResource(R.string.howto_fair),
                    accent = BandJade,
                    lines = listOf(
                        R.drawable.ic_check to stringResource(R.string.howto_fair_1),
                        R.drawable.ic_check to stringResource(R.string.howto_fair_2),
                        R.drawable.ic_check to stringResource(R.string.howto_fair_3),
                        R.drawable.ic_check to stringResource(R.string.howto_fair_4)
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            SecondaryButton(
                text = stringResource(R.string.menu),
                onClick = onMenu,
                accent = AccentCyan,
                height = 48.dp,
                modifier = Modifier.padding(vertical = 12.dp)
            )
        }
    }
}

@Composable
private fun InfoBlock(
    title: String,
    accent: Color,
    lines: List<Pair<Int, String>>
) {
    GlassCard(accent = accent, modifier = Modifier.fillMaxWidth()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(14.dp)
        ) {
            SectionTitle(text = title, accent = accent)
            lines.forEach { line ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(line.first),
                        contentDescription = null,
                        tint = accent,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = line.second,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun ModifierBlock() {
    val rows = listOf(
        Triple(R.drawable.sprite_1, stringResource(R.string.prism_tile), stringResource(R.string.howto_modifier_prism)),
        Triple(R.drawable.sprite_2, stringResource(R.string.cog_tile), stringResource(R.string.howto_modifier_cog)),
        Triple(R.drawable.sprite_3, stringResource(R.string.anchor_tile), stringResource(R.string.howto_modifier_anchor)),
        Triple(R.drawable.sprite_4, stringResource(R.string.flux_tile), stringResource(R.string.howto_modifier_flux))
    )
    GlassCard(accent = AccentViolet, modifier = Modifier.fillMaxWidth()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(14.dp)
        ) {
            SectionTitle(text = stringResource(R.string.howto_modifiers), accent = AccentViolet)
            rows.forEach { row ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(row.first),
                        contentDescription = row.second,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = row.third,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}
