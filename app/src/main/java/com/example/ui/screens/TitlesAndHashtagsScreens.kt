package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.HashtagResult
import com.example.model.TitleSuggestion
import com.example.ui.components.CopyActionButton
import com.example.ui.components.CreatorInputField
import com.example.ui.components.EmptyToolPlaceholder
import com.example.ui.components.NeonPrimaryButton
import com.example.ui.components.OfflineTransparencyBanner
import com.example.ui.components.QuickPresetRow
import com.example.ui.components.ResultGlowCard
import com.example.ui.components.ToolTopHeader

@Composable
fun GamingTitlesScreen(
    input: String,
    results: List<TitleSuggestion>,
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
    val presets = listOf("Valorant Ranked", "Minecraft Hardcore", "Elden Ring Bosses", "PUBG Clutch")

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
            .testTag("gaming_titles_screen")
    ) {
        item {
            ToolTopHeader(
                title = stringResource(R.string.tool_gaming_titles),
                subtitle = stringResource(R.string.tool_gaming_titles_sub),
                icon = Icons.Default.SportsEsports,
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
                        label = stringResource(R.string.label_game_or_topic),
                        placeholder = stringResource(R.string.hint_gaming_titles),
                        testTag = "gaming_titles_input"
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    QuickPresetRow(
                        presets = presets,
                        onPresetSelected = onInputChange
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    NeonPrimaryButton(
                        text = stringResource(R.string.action_generate_titles),
                        onClick = onGenerate,
                        neonGlowEnabled = neonGlowEnabled,
                        testTag = "generate_gaming_titles_button"
                    )
                }
            }
        }

        if (results.isEmpty()) {
            item {
                EmptyToolPlaceholder(
                    icon = Icons.Default.SportsEsports,
                    modifier = Modifier.widthIn(max = 720.dp)
                )
            }
        } else {
            item {
                Row(
                    modifier = Modifier
                        .widthIn(max = 720.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Title Suggestions (${results.size})",
                        style = MaterialTheme.typography.titleMedium,
                        color = cs.onSecondaryContainer
                    )
                    val allTitlesBlock = results.joinToString("\n") { "• ${it.title}" }
                    CopyActionButton(
                        isCopied = lastCopiedId == "titles_copy_all",
                        onClick = { onCopyItem("titles_copy_all", allTitlesBlock) },
                        labelOverride = stringResource(R.string.action_copy_all),
                        testTag = "copy_all_titles_button"
                    )
                }
            }

            items(results, key = { it.id }) { suggestion ->
                ResultGlowCard(
                    badgeText = suggestion.styleBadge,
                    neonGlowEnabled = neonGlowEnabled,
                    headerTrailing = {
                        CopyActionButton(
                            isCopied = lastCopiedId == suggestion.id,
                            onClick = { onCopyItem(suggestion.id, suggestion.title) },
                            testTag = "copy_title_${suggestion.id}"
                        )
                    },
                    modifier = Modifier.widthIn(max = 720.dp)
                ) {
                    Text(
                        text = suggestion.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = cs.onSurface
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HashtagGeneratorScreen(
    input: String,
    result: HashtagResult?,
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
    val presets = listOf("Fortnite Zero Build", "GTA RP", "Indie Horror", "Mobile FPS")

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
            .testTag("hashtag_generator_screen")
    ) {
        item {
            ToolTopHeader(
                title = stringResource(R.string.tool_hashtag_generator),
                subtitle = stringResource(R.string.tool_hashtag_generator_sub),
                icon = Icons.Default.Tag,
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
                        label = stringResource(R.string.label_game_or_topic),
                        placeholder = stringResource(R.string.hint_hashtags),
                        testTag = "hashtag_input"
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    QuickPresetRow(
                        presets = presets,
                        onPresetSelected = onInputChange
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    NeonPrimaryButton(
                        text = stringResource(R.string.action_generate_hashtags),
                        onClick = onGenerate,
                        neonGlowEnabled = neonGlowEnabled,
                        testTag = "generate_hashtags_button"
                    )
                }
            }
        }

        if (result == null || result.allFormattedBlock.isEmpty()) {
            item {
                EmptyToolPlaceholder(
                    icon = Icons.Default.Tag,
                    modifier = Modifier.widthIn(max = 720.dp)
                )
            }
        } else {
            item {
                ResultGlowCard(
                    badgeText = "Full Hashtag Block",
                    neonGlowEnabled = neonGlowEnabled,
                    headerTrailing = {
                        CopyActionButton(
                            isCopied = lastCopiedId == "hashtags_all",
                            onClick = { onCopyItem("hashtags_all", result.allFormattedBlock) },
                            labelOverride = stringResource(R.string.action_copy_all),
                            testTag = "copy_all_hashtags_button"
                        )
                    },
                    modifier = Modifier.widthIn(max = 720.dp)
                ) {
                    Text(
                        text = result.allFormattedBlock,
                        style = MaterialTheme.typography.bodyLarge,
                        color = cs.onSurface
                    )
                }
            }

            item {
                ResultGlowCard(
                    badgeText = "Topic-Specific Tags (Tap Any Tag To Copy)",
                    neonGlowEnabled = neonGlowEnabled,
                    headerTrailing = {
                        val primaryBlock = result.primaryTags.joinToString(" ")
                        CopyActionButton(
                            isCopied = lastCopiedId == "hashtags_primary",
                            onClick = { onCopyItem("hashtags_primary", primaryBlock) },
                            testTag = "copy_primary_hashtags_button"
                        )
                    },
                    modifier = Modifier.widthIn(max = 720.dp)
                ) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        result.primaryTags.forEachIndexed { index, tag ->
                            val tagId = "single_tag_primary_$index"
                            val selected = lastCopiedId == tagId
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = cs.secondaryContainer,
                                border = BorderStroke(
                                    1.dp,
                                    if (selected) cs.primary else cs.outline
                                ),
                                modifier = Modifier
                                    .minimumInteractiveComponentSize()
                                    .clip(RoundedCornerShape(50))
                                    .clickable { onCopyItem(tagId, tag) }
                                    .testTag(tagId)
                            ) {
                                Text(
                                    text = tag,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = cs.onSecondaryContainer,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                                )
                            }
                        }
                    }
                }
            }

            item {
                ResultGlowCard(
                    badgeText = "Creator & Gaming Discovery Tags",
                    neonGlowEnabled = neonGlowEnabled,
                    headerTrailing = {
                        val discoveryBlock = result.discoveryTags.joinToString(" ")
                        CopyActionButton(
                            isCopied = lastCopiedId == "hashtags_discovery",
                            onClick = { onCopyItem("hashtags_discovery", discoveryBlock) },
                            testTag = "copy_discovery_hashtags_button"
                        )
                    },
                    modifier = Modifier.widthIn(max = 720.dp)
                ) {
                    Text(
                        text = result.discoveryTags.joinToString("  "),
                        style = MaterialTheme.typography.bodyMedium,
                        color = cs.onSurfaceVariant
                    )
                }
            }
        }
    }
}
