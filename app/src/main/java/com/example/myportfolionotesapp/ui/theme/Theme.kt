package com.example.myportfolionotesapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme =
    darkColorScheme(
        primary = SmoothDarkPrimary,
        primaryContainer = SmoothDarkPrimaryContainer,
        onPrimary = Color(0xFF001224),
        onPrimaryContainer = SmoothDarkText,
        secondary = SmoothDarkOutline,
        background = SmoothDarkBackground,
        surface = SmoothDarkSurface,
        onBackground = SmoothDarkText,
        onSurface = SmoothDarkText,
        surfaceVariant = Color(0xFF22252A),
        onSurfaceVariant = SmoothDarkOutline,
        outline = SmoothDarkOutline
    )

private val LightColorScheme =
    lightColorScheme(
        primary = SmoothPrimary,
        primaryContainer = SmoothPrimaryContainer,
        onPrimary = Color.White,
        onPrimaryContainer = SmoothPrimary,
        secondary = SmoothSecondary,
        secondaryContainer = SmoothSecondaryContainer,
        outline = SmoothOutline,
        background = SmoothBackground,
        surface = SmoothSurface,
        onBackground = SmoothText,
        onSurface = SmoothText,
        surfaceVariant = SmoothSecondaryContainer,
        onSurfaceVariant = SmoothSecondary
    )

@Composable
fun MyPortfolioNotesAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme =
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }

            darkTheme -> DarkColorScheme
            else -> LightColorScheme
        }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}