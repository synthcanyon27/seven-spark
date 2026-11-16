package com.Rirsefu3.Degmed.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val SevenSparkColors = darkColorScheme(
    primary = AccentViolet,
    onPrimary = TextPrimary,
    primaryContainer = SurfaceRaised,
    onPrimaryContainer = TextPrimary,
    secondary = AccentCyan,
    onSecondary = BackgroundBase,
    secondaryContainer = SurfaceInstrument,
    onSecondaryContainer = TextPrimary,
    tertiary = AccentBrass,
    onTertiary = BackgroundBase,
    tertiaryContainer = SurfaceRaised,
    onTertiaryContainer = BrassLight,
    background = BackgroundBase,
    onBackground = TextPrimary,
    surface = SurfaceDim,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceRaised,
    onSurfaceVariant = TextSecondary,
    outline = TextMuted,
    outlineVariant = DisabledSurface,
    error = DangerDrift,
    onError = TextPrimary
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SevenSparkColors,
        typography = AppTypography,
        content = content
    )
}
