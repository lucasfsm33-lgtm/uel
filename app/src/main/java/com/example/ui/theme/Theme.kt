package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val BlueGptColorScheme = darkColorScheme(
    primary = BlueSecondary,
    onPrimary = Color.White,
    primaryContainer = BlueSurfaceLight,
    onPrimaryContainer = Color.White,
    secondary = BlueAccentCyan,
    onSecondary = Color.Black,
    tertiary = GoldYellow,
    onTertiary = TextOnGold,
    background = BlueBackground,
    onBackground = TextPrimary,
    surface = BlueSurface,
    onSurface = TextPrimary,
    surfaceVariant = BluePrimaryDark,
    onSurfaceVariant = TextSecondary,
    outline = DividerBlue,
    outlineVariant = BlueSurfaceLight
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // We enforce the BlueGPT signature blue theme so the CapCut editor visuals match the mockup perfectly
    MaterialTheme(
        colorScheme = BlueGptColorScheme,
        typography = Typography,
        content = content
    )
}
