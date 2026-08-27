package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = ChemPrimaryDark,
    onPrimary = ChemNavyDark,
    primaryContainer = ChemNavy,
    onPrimaryContainer = ChemCyanLight,
    secondary = ChemSecondaryDark,
    onSecondary = ChemNavyDark,
    secondaryContainer = ChemIndigo.copy(alpha = 0.25f),
    onSecondaryContainer = ChemSecondaryDark,
    tertiary = ChemTertiaryDark,
    onTertiary = ChemNavyDark,
    background = ChemDarkBackground,
    onBackground = ChemTextPrimaryDark,
    surface = ChemDarkSurface,
    onSurface = ChemTextPrimaryDark,
    surfaceVariant = ChemDarkSurfaceVariant,
    onSurfaceVariant = ChemTextSecondaryDark,
    error = ChemRose
)

private val LightColorScheme = lightColorScheme(
    primary = ChemPrimaryLight,
    onPrimary = ChemLightSurface,
    primaryContainer = ChemCyanLight.copy(alpha = 0.2f),
    onPrimaryContainer = ChemPrimaryLight,
    secondary = ChemSecondaryLight,
    onSecondary = ChemLightSurface,
    secondaryContainer = ChemIndigo.copy(alpha = 0.15f),
    onSecondaryContainer = ChemSecondaryLight,
    tertiary = ChemTertiaryLight,
    onTertiary = ChemLightSurface,
    background = ChemLightBackground,
    onBackground = ChemTextPrimaryLight,
    surface = ChemLightSurface,
    onSurface = ChemTextPrimaryLight,
    surfaceVariant = ChemLightSurfaceVariant,
    onSurfaceVariant = ChemTextSecondaryLight,
    error = ChemRose
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // prioritize our custom chemistry theme palette
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
