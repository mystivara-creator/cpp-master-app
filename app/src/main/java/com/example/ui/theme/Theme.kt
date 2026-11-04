package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = CppBluePrimary,
    onPrimary = CppBlueOnPrimary,
    primaryContainer = CppNavyCard,
    onPrimaryContainer = CppBluePrimary,
    secondary = CppIndigoSecondary,
    onSecondary = Color(0xFF1E1B4B),
    secondaryContainer = CppIndigoSecondaryContainer,
    onSecondaryContainer = CppIndigoSecondary,
    tertiary = CppEmeraldTertiary,
    onTertiary = Color(0xFF022C1A),
    background = CppNavyDark,
    onBackground = TextPrimary,
    surface = CppNavySurface,
    onSurface = TextPrimary,
    surfaceVariant = CppNavyCard,
    onSurfaceVariant = TextSecondary,
    outline = CppNavyBorder,
    error = CppRoseError,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = CppBluePrimaryVariant,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F2FE),
    onPrimaryContainer = Color(0xFF0369A1),
    secondary = Color(0xFF4F46E5),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFEEF2FF),
    onSecondaryContainer = Color(0xFF3730A3),
    tertiary = Color(0xFF059669),
    onTertiary = Color.White,
    background = CppLightBackground,
    onBackground = CppLightTextPrimary,
    surface = CppLightSurface,
    onSurface = CppLightTextPrimary,
    surfaceVariant = CppLightCard,
    onSurfaceVariant = CppLightTextSecondary,
    outline = CppLightBorder,
    error = Color(0xFFDC2626),
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to sleek tech dark theme
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.background.toArgb()
                window.navigationBarColor = colorScheme.surface.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
