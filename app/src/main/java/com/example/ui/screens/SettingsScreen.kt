package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.DarkThemeVariant
import com.example.ui.theme.CharcoalBackground
import com.example.ui.theme.CharcoalElevatedCard
import com.example.ui.theme.CharcoalNeonPrimary
import com.example.ui.theme.MidnightBackground
import com.example.ui.theme.MidnightElevatedCard
import com.example.ui.theme.MidnightNeonPrimary
import com.example.ui.theme.NeonPurplePrimary
import com.example.ui.theme.ObsidianBackground
import com.example.ui.theme.ElevatedCardSurface

private data class ThemeOptionPreview(
    val variant: DarkThemeVariant,
    val titleRes: Int,
    val subtitleRes: Int,
    val swatchBg: Color,
    val swatchCard: Color,
    val swatchAccent: Color,
    val testTag: String
)

@Composable
fun SettingsScreen(
    selectedThemeVariant: DarkThemeVariant,
    neonGlowEnabled: Boolean,
    lowercaseHashtags: Boolean,
    includeScriptCues: Boolean,
    onSelectThemeVariant: (DarkThemeVariant) -> Unit,
    onToggleNeonGlow: (Boolean) -> Unit,
    onToggleLowercaseHashtags: (Boolean) -> Unit,
    onToggleIncludeScriptCues: (Boolean) -> Unit,
    onResetWorkspace: () -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme

    val themeOptions = listOf(
        ThemeOptionPreview(
            variant = DarkThemeVariant.OBSIDIAN_PURPLE,
            titleRes = R.string.theme_obsidian_purple,
            subtitleRes = R.string.theme_obsidian_purple_sub,
            swatchBg = ObsidianBackground,
            swatchCard = ElevatedCardSurface,
            swatchAccent = NeonPurplePrimary,
            testTag = "theme_option_obsidian_purple"
        ),
        ThemeOptionPreview(
            variant = DarkThemeVariant.MIDNIGHT_BLUE,
            titleRes = R.string.theme_midnight_blue,
            subtitleRes = R.string.theme_midnight_blue_sub,
            swatchBg = MidnightBackground,
            swatchCard = MidnightElevatedCard,
            swatchAccent = MidnightNeonPrimary,
            testTag = "theme_option_midnight_blue"
        ),
        ThemeOptionPreview(
            variant = DarkThemeVariant.DEEP_CHARCOAL,
            titleRes = R.string.theme_deep_charcoal,
            subtitleRes = R.string.theme_deep_charcoal_sub,
            swatchBg = CharcoalBackground,
            swatchCard = CharcoalElevatedCard,
            swatchAccent = CharcoalNeonPrimary,
            testTag = "theme_option_deep_charcoal"
        )
    )

    LazyColumn(
        contentPadding = PaddingValues(
            top = contentPadding.calculateTopPadding() + 14.dp,
            bottom = contentPadding.calculateBottomPadding() + 28.dp,
            start = 20.dp,
            end = 20.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(cs.background)
            .testTag("settings_screen")
    ) {
        item {
            Column(
                modifier = Modifier
                    .widthIn(max = 720.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.settings_title),
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = cs.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.settings_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = cs.onSurfaceVariant
                )
            }
        }

        // Dark Theme Variation Selector Card
        item {
            Card(
                modifier = Modifier
                    .widthIn(max = 720.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = cs.surfaceVariant),
                border = BorderStroke(
                    1.dp,
                    if (neonGlowEnabled) cs.outlineVariant.copy(alpha = 0.55f) else cs.outline
                )
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = null,
                            tint = cs.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.settings_section_theme),
                            style = MaterialTheme.typography.titleMedium,
                            color = cs.onSecondaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.settings_section_theme_desc),
                        style = MaterialTheme.typography.bodySmall,
                        color = cs.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        themeOptions.forEach { option ->
                            val isSelected = selectedThemeVariant == option.variant
                            ThemeVariantRow(
                                option = option,
                                selected = isSelected,
                                onClick = { onSelectThemeVariant(option.variant) }
                            )
                        }
                    }
                }
            }
        }

        // Workspace Preferences Card
        item {
            Card(
                modifier = Modifier
                    .widthIn(max = 720.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = cs.surfaceVariant),
                border = BorderStroke(
                    1.dp,
                    if (neonGlowEnabled) cs.outlineVariant.copy(alpha = 0.5f) else cs.outline
                )
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = stringResource(R.string.settings_section_preferences),
                        style = MaterialTheme.typography.titleMedium,
                        color = cs.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    PreferenceToggleRow(
                        title = stringResource(R.string.pref_neon_glow_title),
                        description = stringResource(R.string.pref_neon_glow_desc),
                        checked = neonGlowEnabled,
                        onCheckedChange = onToggleNeonGlow,
                        testTag = "switch_neon_glow"
                    )

                    HorizontalDivider(
                        color = cs.outline,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                    PreferenceToggleRow(
                        title = stringResource(R.string.pref_lowercase_tags_title),
                        description = stringResource(R.string.pref_lowercase_tags_desc),
                        checked = lowercaseHashtags,
                        onCheckedChange = onToggleLowercaseHashtags,
                        testTag = "switch_lowercase_tags"
                    )

                    HorizontalDivider(
                        color = cs.outline,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                    PreferenceToggleRow(
                        title = stringResource(R.string.pref_script_cues_title),
                        description = stringResource(R.string.pref_script_cues_desc),
                        checked = includeScriptCues,
                        onCheckedChange = onToggleIncludeScriptCues,
                        testTag = "switch_script_cues"
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    OutlinedButton(
                        onClick = onResetWorkspace,
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, cs.primary.copy(alpha = 0.6f)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = cs.secondaryContainer,
                            contentColor = cs.onSecondaryContainer
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .minimumInteractiveComponentSize()
                            .testTag("reset_workspace_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.RestartAlt,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.pref_clear_workspace),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }

        // Transparency Card
        item {
            Card(
                modifier = Modifier
                    .widthIn(max = 720.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = cs.surface),
                border = BorderStroke(1.dp, cs.outline)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.VerifiedUser,
                            contentDescription = null,
                            tint = cs.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.transparency_card_title),
                            style = MaterialTheme.typography.titleMedium,
                            color = cs.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(R.string.transparency_card_body),
                        style = MaterialTheme.typography.bodySmall,
                        color = cs.onSurfaceVariant
                    )
                }
            }
        }

        item {
            Text(
                text = stringResource(R.string.settings_about_version),
                style = MaterialTheme.typography.labelMedium,
                color = cs.onTertiaryContainer,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun ThemeVariantRow(
    option: ThemeOptionPreview,
    selected: Boolean,
    onClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val containerColor by animateColorAsState(
        targetValue = if (selected) cs.primaryContainer.copy(alpha = 0.24f) else cs.surface,
        label = "themeRowBg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (selected) cs.primary else cs.outline,
        label = "themeRowBorder"
    )

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = containerColor,
        border = BorderStroke(if (selected) 1.5.dp else 1.dp, borderColor),
        modifier = Modifier
            .fillMaxWidth()
            .minimumInteractiveComponentSize()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag(option.testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Layered Color Swatch Preview
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(option.swatchBg)
                    .border(1.dp, option.swatchAccent.copy(alpha = 0.65f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy((-6).dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(option.swatchCard)
                            .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(option.swatchAccent)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(option.titleRes),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = cs.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = stringResource(option.subtitleRes),
                    style = MaterialTheme.typography.bodySmall,
                    color = cs.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Icon(
                imageVector = if (selected) Icons.Filled.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                contentDescription = null,
                tint = if (selected) cs.primary else cs.onTertiaryContainer,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
private fun PreferenceToggleRow(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    testTag: String
) {
    val cs = MaterialTheme.colorScheme
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = cs.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = cs.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = cs.onPrimaryContainer,
                checkedTrackColor = cs.primaryContainer,
                uncheckedThumbColor = cs.onSurfaceVariant,
                uncheckedTrackColor = cs.secondaryContainer,
                uncheckedBorderColor = cs.outline
            ),
            modifier = Modifier
                .minimumInteractiveComponentSize()
                .testTag(testTag)
        )
    }
}
