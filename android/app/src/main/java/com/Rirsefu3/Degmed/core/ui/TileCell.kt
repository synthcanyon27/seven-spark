package com.Rirsefu3.Degmed.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Rirsefu3.Degmed.R
import com.Rirsefu3.Degmed.core.theme.AccentBrass
import com.Rirsefu3.Degmed.core.theme.AccentCyan
import com.Rirsefu3.Degmed.core.theme.AccentViolet
import com.Rirsefu3.Degmed.core.theme.BandJade
import com.Rirsefu3.Degmed.core.theme.NumeralStyle
import com.Rirsefu3.Degmed.core.theme.SurfaceDim
import com.Rirsefu3.Degmed.domain.model.SpectrumBand
import com.Rirsefu3.Degmed.domain.model.Tile
import com.Rirsefu3.Degmed.domain.model.TileModifier

fun bandColor(band: SpectrumBand): Color = when (band) {
    SpectrumBand.VIOLET -> AccentViolet
    SpectrumBand.CYAN -> AccentCyan
    SpectrumBand.BRASS -> AccentBrass
    SpectrumBand.JADE -> BandJade
}

fun modifierSprite(modifier: TileModifier): Int? = when (modifier) {
    TileModifier.PRISM -> R.drawable.sprite_1
    TileModifier.COG -> R.drawable.sprite_2
    TileModifier.ANCHOR -> R.drawable.sprite_3
    TileModifier.FLUX -> R.drawable.sprite_4
    TileModifier.NONE -> null
}

@Composable
fun TileCell(
    tile: Tile,
    cellSize: Dp,
    frozen: Boolean,
    showNumerals: Boolean,
    showBandLabel: Boolean,
    description: String,
    stateLabel: String,
    spriteDescription: String?,
    modifier: Modifier = Modifier
) {
    val accent = bandColor(tile.band)
    val shape = RoundedCornerShape(10.dp)
    val sprite = modifierSprite(tile.modifier)
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(cellSize)
            .padding(2.dp)
            .clip(shape)
            .background(SurfaceDim)
            .background(accent.copy(alpha = if (frozen) 0.12f else 0.2f))
            .border(1.2.dp, accent.copy(alpha = if (frozen) 0.4f else 0.65f), shape)
            .alpha(if (frozen) 0.78f else 1f)
            .semantics {
                contentDescription = description
                stateDescription = stateLabel
            }
    ) {
        if (sprite != null) {
            Image(
                painter = painterResource(sprite),
                contentDescription = spriteDescription,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(cellSize * 0.6f)
            )
        }
        if (showNumerals) {
            Text(
                text = tile.value.toString(),
                style = if (sprite != null) NumeralStyle.copy(fontSize = 13.sp) else NumeralStyle,
                color = accent,
                modifier = Modifier.align(
                    if (sprite != null) Alignment.BottomStart else Alignment.Center
                )
            )
        }
        if (showBandLabel) {
            Text(
                text = stringResource(bandLabelRes(tile.band)),
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = accent.copy(alpha = 0.85f),
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}

private fun bandLabelRes(band: SpectrumBand): Int = when (band) {
    SpectrumBand.VIOLET -> R.string.band_violet
    SpectrumBand.CYAN -> R.string.band_cyan
    SpectrumBand.BRASS -> R.string.band_brass
    SpectrumBand.JADE -> R.string.band_jade
}
