package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.CreatorToolType
import com.example.ui.components.OfflineTransparencyBanner

data class DashboardToolVisual(
    val type: CreatorToolType,
    val titleRes: Int,
    val subtitleRes: Int,
    val icon: ImageVector,
    val accentColors: List<Color>,
    val testTag: String
)

fun getCreatorToolsList(cs: ColorScheme): List<DashboardToolVisual> = listOf(
    DashboardToolVisual(
        type = CreatorToolType.GAMING_TITLES,
        titleRes = R.string.tool_gaming_titles,
        subtitleRes = R.string.tool_gaming_titles_sub,
        icon = Icons.Default.SportsEsports,
        accentColors = listOf(cs.primaryContainer, cs.primary),
        testTag = "tool_card_gaming_titles"
    ),
    DashboardToolVisual(
        type = CreatorToolType.HASHTAG_GENERATOR,
        titleRes = R.string.tool_hashtag_generator,
        subtitleRes = R.string.tool_hashtag_generator_sub,
        icon = Icons.Default.Tag,
        accentColors = listOf(cs.primary, cs.secondary),
        testTag = "tool_card_hashtag_generator"
    ),
    DashboardToolVisual(
        type = CreatorToolType.VOICEOVER_SCRIPTS,
        titleRes = R.string.tool_voiceover_scripts,
        subtitleRes = R.string.tool_voiceover_scripts_sub,
        icon = Icons.Default.Mic,
        accentColors = listOf(cs.primaryContainer, cs.tertiary),
        testTag = "tool_card_voiceover_scripts"
    ),
    DashboardToolVisual(
        type = CreatorToolType.THUMBNAIL_PROMPTS,
        titleRes = R.string.tool_thumbnail_prompts,
        subtitleRes = R.string.tool_thumbnail_prompts_sub,
        icon = Icons.Default.Image,
        accentColors = listOf(cs.secondary, cs.primary),
        testTag = "tool_card_thumbnail_prompts"
    ),
    DashboardToolVisual(
        type = CreatorToolType.VIDEO_IDEAS,
        titleRes = R.string.tool_video_ideas,
        subtitleRes = R.string.tool_video_ideas_sub,
        icon = Icons.Default.Lightbulb,
        accentColors = listOf(cs.primary, cs.primaryContainer),
        testTag = "tool_card_video_ideas"
    )
)

@Composable
fun DashboardScreen(
    neonGlowEnabled: Boolean,
    onOpenTool: (CreatorToolType) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val tools = remember(cs) { getCreatorToolsList(cs) }
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(cs.background)
            .testTag("dashboard_screen")
    ) {
        val isWide = maxWidth >= 640.dp

        LazyColumn(
            contentPadding = PaddingValues(
                top = contentPadding.calculateTopPadding() + 12.dp,
                bottom = contentPadding.calculateBottomPadding() + 24.dp,
                start = 20.dp,
                end = 20.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Column(modifier = Modifier.widthIn(max = 760.dp)) {
                    DashboardHeroCard(neonGlowEnabled = neonGlowEnabled)
                }
            }

            item {
                Column(modifier = Modifier.widthIn(max = 760.dp)) {
                    OfflineTransparencyBanner(neonGlowEnabled = neonGlowEnabled)
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .widthIn(max = 760.dp)
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.section_creator_tools),
                        style = MaterialTheme.typography.titleLarge,
                        color = cs.onBackground
                    )
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = cs.secondaryContainer,
                        border = BorderStroke(1.dp, cs.outline)
                    ) {
                        Text(
                            text = "5 TOOLS",
                            style = MaterialTheme.typography.labelSmall,
                            color = cs.onSecondaryContainer,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            if (isWide) {
                val chunked = tools.chunked(2)
                items(chunked.size) { rowIndex ->
                    val pair = chunked[rowIndex]
                    Row(
                        modifier = Modifier
                            .widthIn(max = 760.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        pair.forEach { tool ->
                            CreatorToolDashboardCard(
                                tool = tool,
                                neonGlowEnabled = neonGlowEnabled,
                                onClick = { onOpenTool(tool.type) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (pair.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            } else {
                items(tools.size) { index ->
                    val tool = tools[index]
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(tween(240, delayMillis = index * 45)) +
                            slideInVertically(
                                animationSpec = tween(260, delayMillis = index * 45),
                                initialOffsetY = { it / 4 }
                            )
                    ) {
                        CreatorToolDashboardCard(
                            tool = tool,
                            neonGlowEnabled = neonGlowEnabled,
                            onClick = { onOpenTool(tool.type) },
                            modifier = Modifier.widthIn(max = 760.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DashboardHeroCard(
    neonGlowEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val borderBrush = if (neonGlowEnabled) {
        Brush.linearGradient(
            colors = listOf(
                cs.primary.copy(alpha = 0.85f),
                cs.primaryContainer.copy(alpha = 0.45f),
                cs.secondary.copy(alpha = 0.65f)
            )
        )
    } else {
        Brush.linearGradient(listOf(cs.outline, cs.outline))
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.5.dp, borderBrush, RoundedCornerShape(26.dp)),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = cs.surfaceVariant)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.img_creator_hero_banner),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                cs.background.copy(alpha = 0.92f),
                                cs.background.copy(alpha = 0.74f),
                                cs.primaryContainer.copy(alpha = 0.42f)
                            )
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = cs.primaryContainer.copy(alpha = 0.28f),
                        border = BorderStroke(1.dp, cs.primary.copy(alpha = 0.65f))
                    ) {
                        Text(
                            text = stringResource(R.string.dashboard_badge),
                            style = MaterialTheme.typography.labelSmall,
                            color = cs.onSecondaryContainer,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                        color = cs.onBackground
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = stringResource(R.string.dashboard_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = cs.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .border(1.dp, cs.primary.copy(alpha = 0.7f), RoundedCornerShape(18.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_sn_launcher_logo),
                        contentDescription = stringResource(R.string.splash_logo_desc),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun CreatorToolDashboardCard(
    tool: DashboardToolVisual,
    neonGlowEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val borderBrush = if (neonGlowEnabled) {
        Brush.horizontalGradient(
            colors = listOf(
                tool.accentColors.first().copy(alpha = 0.75f),
                cs.outline,
                tool.accentColors.last().copy(alpha = 0.45f)
            )
        )
    } else {
        Brush.horizontalGradient(listOf(cs.outline, cs.outline))
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .border(1.dp, borderBrush, RoundedCornerShape(22.dp))
            .clickable(onClick = onClick)
            .testTag(tool.testTag),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = cs.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    if (neonGlowEnabled) {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    tool.accentColors.first().copy(alpha = 0.22f),
                                    Color.Transparent
                                ),
                                center = Offset(36.dp.toPx(), size.height / 2f),
                                radius = 92.dp.toPx()
                            ),
                            center = Offset(36.dp.toPx(), size.height / 2f),
                            radius = 92.dp.toPx()
                        )
                    }
                }
                .padding(horizontal = 18.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(colors = tool.accentColors)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = tool.icon,
                    contentDescription = null,
                    tint = cs.onPrimaryContainer,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(tool.titleRes),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = cs.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(tool.subtitleRes),
                    style = MaterialTheme.typography.bodySmall,
                    color = cs.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(cs.secondaryContainer)
                    .border(1.dp, cs.outlineVariant.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = cs.onSecondaryContainer
                )
            }
        }
    }
}
