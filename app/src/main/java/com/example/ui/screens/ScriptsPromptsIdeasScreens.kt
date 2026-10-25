package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CropLandscape
import androidx.compose.material.icons.filled.CropPortrait
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.ScriptLength
import com.example.model.ScriptResult
import com.example.model.ThumbnailAspectRatio
import com.example.model.ThumbnailPromptItem
import com.example.model.VideoIdeaItem
import com.example.ui.components.CopyActionButton
import com.example.ui.components.CreatorInputField
import com.example.ui.components.EmptyToolPlaceholder
import com.example.ui.components.NeonPrimaryButton
import com.example.ui.components.OfflineTransparencyBanner
import com.example.ui.components.QuickPresetRow
import com.example.ui.components.ResultGlowCard
import com.example.ui.components.ToolTopHeader

@Composable
fun VoiceoverScriptsScreen(
    input: String,
    selectedLength: ScriptLength,
    scriptResult: ScriptResult?,
    neonGlowEnabled: Boolean,
    lastCopiedId: String?,
    onInputChange: (String) -> Unit,
    onLengthSelect: (ScriptLength) -> Unit,
    onGenerate: () -> Unit,
    onCopyItem: (String, String) -> Unit,
    onBack: () -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val cs = MaterialTheme.colorScheme

    val presets = listOf(
        "Surviving 100 days on a hardcore island with zero armor",
        "Climbing from Bronze to Immortal solo queue",
        "Speedrunning the hardest boss blindfolded"
    )

    LazyColumn(
        contentPadding = PaddingValues(
            top = contentPadding.calculateTopPadding() + 8.dp,
            bottom = contentPadding.calculateBottomPadding() + 28.dp,
            start = 20.dp,
            end = 20.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(cs.background)
            .testTag("voiceover_scripts_screen")
    ) {
        item {
            ToolTopHeader(
                title = stringResource(R.string.tool_voiceover_scripts),
                subtitle = stringResource(R.string.tool_voiceover_scripts_sub),
                icon = Icons.Default.Mic,
                onBack = onBack,
                modifier = Modifier.widthIn(max = 720.dp)
            )
        }

        item {
            OfflineTransparencyBanner(
                neonGlowEnabled = neonGlowEnabled,
                modifier = Modifier.widthIn(max = 720.dp)
            )
        }

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
                    CreatorInputField(
                        value = input,
                        onValueChange = onInputChange,
                        label = stringResource(R.string.label_video_details),
                        placeholder = stringResource(R.string.hint_voiceover),
                        minLines = 2,
                        maxLines = 4,
                        testTag = "voiceover_details_input"
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    QuickPresetRow(
                        presets = presets,
                        onPresetSelected = onInputChange
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = stringResource(R.string.label_script_length),
                        style = MaterialTheme.typography.labelLarge,
                        color = cs.onSecondaryContainer,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ScriptLengthOptionChip(
                            title = stringResource(R.string.length_short),
                            subtitle = stringResource(R.string.length_short_sub),
                            selected = selectedLength == ScriptLength.SHORT,
                            onClick = { onLengthSelect(ScriptLength.SHORT) },
                            testTag = "length_option_short",
                            modifier = Modifier.weight(1f)
                        )
                        ScriptLengthOptionChip(
                            title = stringResource(R.string.length_medium),
                            subtitle = stringResource(R.string.length_medium_sub),
                            selected = selectedLength == ScriptLength.MEDIUM,
                            onClick = { onLengthSelect(ScriptLength.MEDIUM) },
                            testTag = "length_option_medium",
                            modifier = Modifier.weight(1f)
                        )
                        ScriptLengthOptionChip(
                            title = stringResource(R.string.length_long),
                            subtitle = stringResource(R.string.length_long_sub),
                            selected = selectedLength == ScriptLength.LONG,
                            onClick = { onLengthSelect(ScriptLength.LONG) },
                            testTag = "length_option_long",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    NeonPrimaryButton(
                        text = stringResource(R.string.action_generate_script),
                        onClick = onGenerate,
                        neonGlowEnabled = neonGlowEnabled,
                        testTag = "generate_script_button"
                    )
                }
            }
        }

        if (scriptResult == null) {
            item {
                EmptyToolPlaceholder(
                    icon = Icons.Default.Mic,
                    modifier = Modifier.widthIn(max = 720.dp)
                )
            }
        } else {
            item {
                ResultGlowCard(
                    badgeText = scriptResult.estimatedDuration,
                    neonGlowEnabled = neonGlowEnabled,
                    headerTrailing = {
                        CopyActionButton(
                            isCopied = lastCopiedId == "script_result_copy",
                            onClick = { onCopyItem("script_result_copy", scriptResult.fullScript) },
                            testTag = "copy_script_button"
                        )
                    },
                    modifier = Modifier.widthIn(max = 720.dp)
                ) {
                    Text(
                        text = scriptResult.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = cs.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = scriptResult.fullScript,
                        style = MaterialTheme.typography.bodyLarge,
                        color = cs.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun ScriptLengthOptionChip(
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val bgColor by animateColorAsState(
        targetValue = if (selected) cs.primaryContainer.copy(alpha = 0.28f) else cs.secondaryContainer,
        label = "scriptLengthBg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (selected) cs.primary else cs.outline,
        label = "scriptLengthBorder"
    )

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = bgColor,
        border = BorderStroke(if (selected) 1.5.dp else 1.dp, borderColor),
        modifier = modifier
            .minimumInteractiveComponentSize()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = if (selected) cs.onSurface else cs.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = if (selected) cs.onSecondaryContainer else cs.onTertiaryContainer
            )
        }
    }
}

@Composable
fun ThumbnailPromptsScreen(
    input: String,
    selectedRatio: ThumbnailAspectRatio,
    results: List<ThumbnailPromptItem>,
    neonGlowEnabled: Boolean,
    lastCopiedId: String?,
    onInputChange: (String) -> Unit,
    onRatioSelect: (ThumbnailAspectRatio) -> Unit,
    onGenerate: () -> Unit,
    onCopyItem: (String, String) -> Unit,
    onBack: () -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val cs = MaterialTheme.colorScheme

    val presets = listOf(
        "Cyberpunk samurai facing a giant neon mech in the rain",
        "Shocked gamer character surrounded by golden treasure chests",
        "Dark fantasy knight standing before a glowing purple portal"
    )

    LazyColumn(
        contentPadding = PaddingValues(
            top = contentPadding.calculateTopPadding() + 8.dp,
            bottom = contentPadding.calculateBottomPadding() + 28.dp,
            start = 20.dp,
            end = 20.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(cs.background)
            .testTag("thumbnail_prompts_screen")
    ) {
        item {
            ToolTopHeader(
                title = stringResource(R.string.tool_thumbnail_prompts),
                subtitle = stringResource(R.string.tool_thumbnail_prompts_sub),
                icon = Icons.Default.Image,
                onBack = onBack,
                modifier = Modifier.widthIn(max = 720.dp)
            )
        }

        item {
            OfflineTransparencyBanner(
                neonGlowEnabled = neonGlowEnabled,
                modifier = Modifier.widthIn(max = 720.dp)
            )
        }

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
                    CreatorInputField(
                        value = input,
                        onValueChange = onInputChange,
                        label = stringResource(R.string.label_thumbnail_idea),
                        placeholder = stringResource(R.string.hint_thumbnail),
                        minLines = 2,
                        maxLines = 3,
                        testTag = "thumbnail_idea_input"
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    QuickPresetRow(
                        presets = presets,
                        onPresetSelected = onInputChange
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = stringResource(R.string.label_aspect_ratio),
                        style = MaterialTheme.typography.labelLarge,
                        color = cs.onSecondaryContainer,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AspectRatioOptionCard(
                            title = stringResource(R.string.ratio_9_16),
                            subtitle = stringResource(R.string.ratio_9_16_sub),
                            icon = Icons.Default.CropPortrait,
                            selected = selectedRatio == ThumbnailAspectRatio.VERTICAL_9_16,
                            onClick = { onRatioSelect(ThumbnailAspectRatio.VERTICAL_9_16) },
                            testTag = "ratio_option_9_16",
                            modifier = Modifier.weight(1f)
                        )
                        AspectRatioOptionCard(
                            title = stringResource(R.string.ratio_16_9),
                            subtitle = stringResource(R.string.ratio_16_9_sub),
                            icon = Icons.Default.CropLandscape,
                            selected = selectedRatio == ThumbnailAspectRatio.WIDESCREEN_16_9,
                            onClick = { onRatioSelect(ThumbnailAspectRatio.WIDESCREEN_16_9) },
                            testTag = "ratio_option_16_9",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    NeonPrimaryButton(
                        text = stringResource(R.string.action_generate_prompts),
                        onClick = onGenerate,
                        neonGlowEnabled = neonGlowEnabled,
                        testTag = "generate_thumbnail_prompts_button"
                    )
                }
            }
        }

        if (results.isEmpty()) {
            item {
                EmptyToolPlaceholder(
                    icon = Icons.Default.Image,
                    modifier = Modifier.widthIn(max = 720.dp)
                )
            }
        } else {
            items(results, key = { it.id }) { item ->
                ResultGlowCard(
                    badgeText = "${item.styleBadge} • ${item.aspectRatio.ratioLabel}",
                    neonGlowEnabled = neonGlowEnabled,
                    headerTrailing = {
                        CopyActionButton(
                            isCopied = lastCopiedId == item.id,
                            onClick = { onCopyItem(item.id, item.promptText) },
                            testTag = "copy_prompt_${item.id}"
                        )
                    },
                    modifier = Modifier.widthIn(max = 720.dp)
                ) {
                    Text(
                        text = item.promptText,
                        style = MaterialTheme.typography.bodyLarge,
                        color = cs.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun AspectRatioOptionCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val bgColor by animateColorAsState(
        targetValue = if (selected) cs.primaryContainer.copy(alpha = 0.28f) else cs.secondaryContainer,
        label = "ratioBg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (selected) cs.primary else cs.outline,
        label = "ratioBorder"
    )

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = bgColor,
        border = BorderStroke(if (selected) 1.5.dp else 1.dp, borderColor),
        modifier = modifier
            .minimumInteractiveComponentSize()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) cs.primary else cs.onSurfaceVariant,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = if (selected) cs.onSurface else cs.onSurfaceVariant
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (selected) cs.onSecondaryContainer else cs.onTertiaryContainer
                )
            }
        }
    }
}

@Composable
fun VideoIdeasScreen(
    input: String,
    results: List<VideoIdeaItem>,
    neonGlowEnabled: Boolean,
    lastCopiedId: String?,
    onInputChange: (String) -> Unit,
    onGenerate: () -> Unit,
    onCopyItem: (String, String) -> Unit,
    onBack: () -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val cs = MaterialTheme.colorScheme
    val presets = listOf("Sim Racing", "Minecraft Redstone", "Battle Royale", "Cozy Indie Games")

    LazyColumn(
        contentPadding = PaddingValues(
            top = contentPadding.calculateTopPadding() + 8.dp,
            bottom = contentPadding.calculateBottomPadding() + 28.dp,
            start = 20.dp,
            end = 20.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(cs.background)
            .testTag("video_ideas_screen")
    ) {
        item {
            ToolTopHeader(
                title = stringResource(R.string.tool_video_ideas),
                subtitle = stringResource(R.string.tool_video_ideas_sub),
                icon = Icons.Default.Lightbulb,
                onBack = onBack,
                modifier = Modifier.widthIn(max = 720.dp)
            )
        }

        item {
            OfflineTransparencyBanner(
                neonGlowEnabled = neonGlowEnabled,
                modifier = Modifier.widthIn(max = 720.dp)
            )
        }

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
                    CreatorInputField(
                        value = input,
                        onValueChange = onInputChange,
                        label = stringResource(R.string.label_game_or_niche),
                        placeholder = stringResource(R.string.hint_video_ideas),
                        testTag = "video_ideas_input"
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    QuickPresetRow(
                        presets = presets,
                        onPresetSelected = onInputChange
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    NeonPrimaryButton(
                        text = stringResource(R.string.action_generate_ideas),
                        onClick = onGenerate,
                        neonGlowEnabled = neonGlowEnabled,
                        testTag = "generate_video_ideas_button"
                    )
                }
            }
        }

        if (results.isEmpty()) {
            item {
                EmptyToolPlaceholder(
                    icon = Icons.Default.Lightbulb,
                    modifier = Modifier.widthIn(max = 720.dp)
                )
            }
        } else {
            items(results, key = { it.id }) { idea ->
                val fullIdeaText = "${idea.hookTitle}\nAngle: ${idea.conceptOutline}"
                ResultGlowCard(
                    badgeText = idea.formatBadge,
                    neonGlowEnabled = neonGlowEnabled,
                    headerTrailing = {
                        CopyActionButton(
                            isCopied = lastCopiedId == idea.id,
                            onClick = { onCopyItem(idea.id, fullIdeaText) },
                            testTag = "copy_idea_${idea.id}"
                        )
                    },
                    modifier = Modifier.widthIn(max = 720.dp)
                ) {
                    Text(
                        text = idea.hookTitle,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = cs.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = idea.conceptOutline,
                        style = MaterialTheme.typography.bodyMedium,
                        color = cs.onSurfaceVariant
                    )
                }
            }
        }
    }
}
