package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = SnapdragonRedBright,
    onPrimary = Color.White,
    primaryContainer = SnapdragonRedDim,
    onPrimaryContainer = Color.White,
    secondary = HexagonCyan,
    onSecondary = Color(0xFF00363D),
    secondaryContainer = Color(0xFF004F59),
    onSecondaryContainer = Color(0xFF97F0FF),
    tertiary = AmberGold,
    onTertiary = Color(0xFF3F2E00),
    background = TechNavyDark,
    onBackground = TextPrimaryDark,
    surface = TechNavyCard,
    onSurface = TextPrimaryDark,
    surfaceVariant = TechNavySurface,
    onSurfaceVariant = TextSecondaryDark,
    outline = TechNavyBorder
)

private val LightColorScheme = lightColorScheme(
    primary = SnapdragonRed,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDAD6),
    onPrimaryContainer = Color(0xFF410002),
    secondary = HexagonCyanDim,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFC4E7EB),
    onSecondaryContainer = Color(0xFF002022),
    tertiary = Color(0xFF8B5000),
    onTertiary = Color.White,
    background = TechNavyLightBg,
    onBackground = TextPrimaryLight,
    surface = TechNavyLightCard,
    onSurface = TextPrimaryLight,
    surfaceVariant = TechNavyLightSurface,
    onSurfaceVariant = TextSecondaryLight,
    outline = TechNavyLightBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our branded Snapdragon theme by default for cohesive tech identity
    content: @Composable () -> Unit,
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
