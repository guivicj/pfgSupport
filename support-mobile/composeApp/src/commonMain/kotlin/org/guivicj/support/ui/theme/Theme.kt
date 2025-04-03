package org.guivicj.support.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = md_theme_light_primary,
    secondary = md_theme_light_secondary,
    tertiary = md_theme_light_tertiary,
    background = md_theme_light_background,
    surface = md_theme_light_surface,
    outline = md_theme_light_outline,
    onBackground = md_theme_light_onBackground,
    onSurface = md_theme_light_onSurface,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    tertiaryContainer = md_theme_light_initialBackgroundGradient,
    onTertiaryContainer = md_theme_light_finalBackgroundGradient
)

private val DarkColors = darkColorScheme(
    primary = md_theme_dark_primary,
    secondary = md_theme_dark_secondary,
    tertiary = md_theme_dark_tertiary,
    background = md_theme_dark_background,
    surface = md_theme_dark_surface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    inverseSurface = md_theme_dark_inverseSurface,
    outline = md_theme_dark_outline,
    outlineVariant = md_theme_dark_outlineVariant,
    onBackground = md_theme_dark_onBackground,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    tertiaryContainer = md_theme_dark_initialBackgroundGradient,
    onTertiaryContainer = md_theme_dark_finalBackgroundGradient
)

@Composable
fun AppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (useDarkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
