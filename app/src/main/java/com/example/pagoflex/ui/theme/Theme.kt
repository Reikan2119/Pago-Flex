package com.example.pagoflex.ui.theme

import android.app.Activity
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
    primary = PrimarioDark,
    secondary = SecundarioDark,
    tertiary = AcentoDark,
    background = FondoDark,
    surface = SuperficieDark,
    surfaceVariant = SuperficieVarianteDark,
    error = ErrorColor
)

private val LightColorScheme = lightColorScheme(
    primary = Primario,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFBCE6DD),
    onPrimaryContainer = Color(0xFF0B3D35),
    secondary = Secundario,
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFD6E6F5),
    onSecondaryContainer = Color(0xFF123A5C),
    tertiary = Acento,
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFE2CC),
    onTertiaryContainer = Color(0xFF5C3100),
    background = Fondo,
    onBackground = Color(0xFF1A2825),
    surface = Superficie,
    onSurface = Color(0xFF1A2825),
    surfaceVariant = SuperficieVariante,
    onSurfaceVariant = Color(0xFF3F5550),
    outline = Color(0xFF9FB3AE),
    error = ErrorColor,
    onError = Color(0xFFFFFFFF)
)

@Composable
fun PagoFlexTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
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
        shapes = Shapes,
        content = content
    )
}