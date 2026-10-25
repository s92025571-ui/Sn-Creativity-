package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.CopiedItemRecord
import com.example.model.CreatorToolType
import com.example.ui.components.CopyActionButton
import com.example.ui.components.ResultGlowCard

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateHubScreen(
    neonGlowEnabled: Boolean,
    recentCopiedItems: List<CopiedItemRecord>,
    lastCopiedId: String?,
    onOpenTool: (CreatorToolType) -> Unit,
    onCopyText: (String, String, String) -> Unit,
    onClearRecent: () -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val tools = remember(cs) { getCreatorToolsList(cs) }

    LazyColumn(
        contentPadding = PaddingValues(
            top = contentPadding.calculateTopPadding() + 14.dp,
            bottom = contentPadding.calculateBottomPadding() + 24.dp,
            start = 20.dp,
            end = 20.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(cs.background)
            .testTag("create_hub_screen")
    ) {
        item {
            Column(
                modifier = Modifier
                    .widthIn(max = 720.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.create_hub_title),
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = cs.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.create_hub_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = cs.onSurfaceVariant
                )
            }
        }

        item {
            Column(
                modifier = Modifier
                    .widthIn(max = 720.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.create_quick_launch),
                    style = MaterialTheme.typography.titleMedium,
                    color = cs.onSecondaryContainer,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    tools.forEach { tool ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = cs.surfaceVariant,
                            border = BorderStroke(
                                1.dp,
                                if (neonGlowEnabled) cs.outlineVariant.copy(alpha = 0.55f) else cs.outline
                            ),
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { onOpenTool(tool.type) }
                                .testTag("quick_launch_${tool.type.id}")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Brush.linearGradient(tool.accentColors)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = tool.icon,
                                        contentDescription = null,
                                        tint = cs.onPrimaryContainer,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = stringResource(tool.titleRes),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = cs.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier
                    .widthIn(max = 720.dp)
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.create_recent_copies),
                    style = MaterialTheme.typography.titleMedium,
                    color = cs.onSecondaryContainer
                )
                if (recentCopiedItems.isNotEmpty()) {
                    TextButton(
                        onClick = onClearRecent,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("clear_recent_copies_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteSweep,
                            contentDescription = null,
                            tint = cs.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.action_clear_recent),
                            style = MaterialTheme.typography.labelLarge,
                            color = cs.primary
                        )
                    }
                }
            }
        }

        if (recentCopiedItems.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .widthIn(max = 720.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = cs.surface),
                    border = BorderStroke(1.dp, cs.outline)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(cs.secondaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentPaste,
                                contentDescription = null,
                                tint = cs.primary
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = stringResource(R.string.create_empty_copies),
                            style = MaterialTheme.typography.bodySmall,
                            color = cs.onSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(recentCopiedItems, key = { it.id }) { record ->
                ResultGlowCard(
                    badgeText = record.toolName,
                    neonGlowEnabled = neonGlowEnabled,
                    headerTrailing = {
                        CopyActionButton(
                            isCopied = lastCopiedId == record.id,
                            onClick = { onCopyText(record.id, record.toolName, record.content) },
                            testTag = "recopy_item_${record.id}"
                        )
                    },
                    modifier = Modifier.widthIn(max = 720.dp)
                ) {
                    Text(
                        text = record.content,
                        style = MaterialTheme.typography.bodyMedium,
                        color = cs.onSurface,
                        maxLines = 5,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
