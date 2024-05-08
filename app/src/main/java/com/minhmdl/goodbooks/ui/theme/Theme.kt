package com.minhmdl.goodbooks.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun GoodbooksTheme(darkTheme: Boolean = isSystemInDarkTheme(),
                   dynamicColor: Boolean = true,
                   content: @Composable () -> Unit) {

    val darkColorScheme = darkColorScheme(
        background = Color.Black,
        onBackground = DarkWhite,
        primary = Pink200,
        surface = Color.Black,
        secondary = Gray,
        onSecondaryContainer = DarkWhite,
        tertiary = DarkWhite,
        primaryContainer = Color.Black,
        onTertiaryContainer = Gray700.copy(alpha = 0.8f),
        surfaceVariant = DarkWhite,
        outline = iconColorDarkMode
    )

    val lightColorScheme = lightColorScheme(
        background = Color.White,
        onBackground = Gray700.copy(alpha = 0.8f),
        primary = White,
        surface = Color.White,
        secondary = Pink200,
        onSecondaryContainer = Color.White,
        tertiary = Pink700,
        primaryContainer = White,
        onTertiaryContainer = Gray700.copy(alpha = 0.1f),
        surfaceVariant = Color.White,
        outline = iconColor
    )

    val colors = if (darkTheme) darkColorScheme else lightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val activity  = view.context as Activity
            activity.window.navigationBarColor = colors.primary.copy(alpha = 0.08f).compositeOver(colors.surface.copy()).toArgb()
            activity.window.statusBarColor = colors.background.toArgb()
            WindowCompat.getInsetsController(activity.window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(activity.window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colors,
        shapes = Shapes,
        content = content
    )
}

