package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.example.model.DarkThemeVariant

private val ObsidianPurpleColorScheme = darkColorScheme(
    primary = NeonPurplePrimary,
    onPrimary = ObsidianBackground,
    primaryContainer = ElectricViolet,
    onPrimaryContainer = TextPureWhite,
    secondary = NeonMagentaAccent,
    onSecondary = TextPureWhite,
    secondaryContainer = SubtleSurfaceContainer,
    onSecondaryContainer = SoftLavenderGlow,
    tertiary = CyanSparkAccent,
    onTertiary = ObsidianBackground,
    background = ObsidianBackground,
    onBackground = TextPureWhite,
    surface = DeepStudioSurface,
    onSurface = TextPureWhite,
    surfaceVariant = ElevatedCardSurface,
    onSurfaceVariant = TextSecondaryLavender,
    surfaceContainerLowest = ObsidianBackground,
    surfaceContainerLow = DeepStudioSurface,
    surfaceContainer = ElevatedCardSurface,
    surfaceContainerHigh = SubtleSurfaceContainer,
    outline = CardBorderSubtle,
    outlineVariant = CardBorderGlow,
    onTertiaryContainer = TextMutedSlate
)

private val MidnightBlueColorScheme = darkColorScheme(
    primary = MidnightNeonPrimary,
    onPrimary = MidnightBackground,
    primaryContainer = MidnightElectricAccent,
    onPrimaryContainer = MidnightTextPrimary,
    secondary = MidnightSecondaryAccent,
    onSecondary = MidnightTextPrimary,
    secondaryContainer = MidnightSubtleContainer,
    onSecondaryContainer = MidnightSoftGlow,
    tertiary = MidnightCyanSpark,
    onTertiary = MidnightBackground,
    background = MidnightBackground,
    onBackground = MidnightTextPrimary,
    surface = MidnightStudioSurface,
    onSurface = MidnightTextPrimary,
    surfaceVariant = MidnightElevatedCard,
    onSurfaceVariant = MidnightTextSecondary,
    surfaceContainerLowest = MidnightBackground,
    surfaceContainerLow = MidnightStudioSurface,
    surfaceContainer = MidnightElevatedCard,
    surfaceContainerHigh = MidnightSubtleContainer,
    outline = MidnightBorderSubtle,
    outlineVariant = MidnightBorderGlow,
    onTertiaryContainer = MidnightTextMuted
)

private val DeepCharcoalColorScheme = darkColorScheme(
    primary = CharcoalNeonPrimary,
    onPrimary = CharcoalBackground,
    primaryContainer = CharcoalElectricAccent,
    onPrimaryContainer = CharcoalTextPrimary,
    secondary = CharcoalSecondaryAccent,
    onSecondary = CharcoalTextPrimary,
    secondaryContainer = CharcoalSubtleContainer,
    onSecondaryContainer = CharcoalSoftGlow,
    tertiary = CharcoalCyanSpark,
    onTertiary = CharcoalBackground,
    background = CharcoalBackground,
    onBackground = CharcoalTextPrimary,
    surface = CharcoalStudioSurface,
    onSurface = CharcoalTextPrimary,
    surfaceVariant = CharcoalElevatedCard,
    onSurfaceVariant = CharcoalTextSecondary,
    surfaceContainerLowest = CharcoalBackground,
    surfaceContainerLow = CharcoalStudioSurface,
    surfaceContainer = CharcoalElevatedCard,
    surfaceContainerHigh = CharcoalSubtleContainer,
    outline = CharcoalBorderSubtle,
    outlineVariant = CharcoalBorderGlow,
    onTertiaryContainer = CharcoalTextMuted
)

fun colorSchemeForVariant(variant: DarkThemeVariant): ColorScheme = when (variant) {
    DarkThemeVariant.OBSIDIAN_PURPLE -> ObsidianPurpleColorScheme
    DarkThemeVariant.MIDNIGHT_BLUE -> MidnightBlueColorScheme
    DarkThemeVariant.DEEP_CHARCOAL -> DeepCharcoalColorScheme
}

val SnShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(30.dp)
)

@Composable
fun SnCreativityTheme(
    themeVariant: DarkThemeVariant = DarkThemeVariant.OBSIDIAN_PURPLE,
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                WindowCompat.getInsetsController(window, view).apply {
                    isAppearanceLightStatusBars = false
                    isAppearanceLightNavigationBars = false
                }
            }
        }
    }

    MaterialTheme(
        colorScheme = colorSchemeForVariant(themeVariant),
        typography = Typography,
        shapes = SnShapes,
        content = content
    )
}
