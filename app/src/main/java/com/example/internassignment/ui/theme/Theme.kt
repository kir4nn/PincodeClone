package com.example.internassignment.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme


private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF9CABF6),        // Your primary color
    surfaceContainerHighest = Color(0xFFE6E9F6),
    secondary = Color.White,
    tertiary = Color.Black,
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    surfaceVariant = Color.White,
    outline = Color.Black.copy(alpha = 0.12f),
    onSurfaceVariant = Color.Black.copy(alpha = 0.6f),
    surfaceContainerLow = Color(0xFFF6F6F6),
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF9CABF6),        // Keep same primary for consistency
    surfaceContainerHighest = Color(0xFF758AFA),
    secondary = Color(0xFF2C2C2C),      // Dark gray for secondary surfaces
    tertiary = Color.White,
    background = Color(0xFF121212),     // Material dark background
    surface = Color(0xFF1E1E1E),        // Slightly lighter than background
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    surfaceVariant = Color(0xFF2C2C2C),
    outline = Color.White.copy(alpha = 0.12f),
    onSurfaceVariant = Color.White.copy(alpha = 0.6f),
    surfaceContainerLow = Color(0xFF1E1E1E),
)

@Composable
fun InternAssignmentTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}