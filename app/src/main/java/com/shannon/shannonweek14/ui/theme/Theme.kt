package com.shannon.shannonweek14.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Light Theme
private val LightColors = lightColorScheme(
    primary = Color(0xFFDBDAAE),
    onPrimary = Color.Black,
    background = Color.White,
    onBackground = Color.Black
)

// Dark Theme
private val DarkColors = darkColorScheme(
    primary = Color(0xFFDBDAAE),
    onPrimary = Color.Black,
    background = Color.Black,
    onBackground = Color.White
)

@Composable
fun Theme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    // Typography default dari Material3
    MaterialTheme(
        colorScheme = colors,
        typography = Typography(),
        content = content
    )
}
