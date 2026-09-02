package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = ShinobuLilac,
    onPrimary = ShinobuDarkBg,
    primaryContainer = ShinobuDeepPurple,
    onPrimaryContainer = ShinobuLavenderLight,
    secondary = ShinobuMint,
    onSecondary = ShinobuTealDark,
    tertiary = ShinobuPinkAccent,
    background = ShinobuDarkBg,
    surface = Color(0xFF261234),
    onBackground = TextLight,
    onSurface = TextLight,
  )

private val LightColorScheme =
  lightColorScheme(
    primary = ShinobuVioletPrimary,
    onPrimary = Color.White,
    primaryContainer = ShinobuLavenderLight,
    onPrimaryContainer = ShinobuDeepPurple,
    secondary = ShinobuMint,
    onSecondary = ShinobuTealDark,
    tertiary = ShinobuPinkAccent,
    background = Color(0xFFF9F5FF),
    surface = ShinobuCardBg,
    onBackground = TextDark,
    onSurface = TextDark,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Keep bespoke Shinobu purple aesthetic
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

