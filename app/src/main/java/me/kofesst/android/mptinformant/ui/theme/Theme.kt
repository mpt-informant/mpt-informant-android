package me.kofesst.android.mptinformant.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import me.kofesst.android.mptinformant.domain.models.WeekLabel

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    useWeekLabelTheme: Boolean = false,
    weekLabel: WeekLabel = WeekLabel.None,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        darkTheme -> DefaultDarkColorScheme
        else -> DefaultLightColorScheme
    }.run {
        if (useWeekLabelTheme) {
            toWeekLabelScheme(weekLabel)
        } else {
            this
        }
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
