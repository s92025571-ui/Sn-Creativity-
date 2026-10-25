package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.theme.SuccessMint

@Composable
fun ToolTopHeader(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .minimumInteractiveComponentSize()
                .clip(CircleShape)
                .background(cs.surfaceVariant)
                .border(1.dp, cs.outline, CircleShape)
                .testTag("tool_back_button")
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.cd_back),
                tint = cs.onSurface
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(cs.primaryContainer, cs.primary)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = cs.onPrimaryContainer,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = cs.onBackground
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = cs.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}

@Composable
fun OfflineTransparencyBanner(
    neonGlowEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val borderBrush = if (neonGlowEnabled) {
        Brush.horizontalGradient(
            listOf(
                cs.outlineVariant.copy(alpha = 0.65f),
                cs.outline,
                cs.secondary.copy(alpha = 0.45f)
            )
        )
    } else {
        Brush.horizontalGradient(listOf(cs.outline, cs.outline))
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, borderBrush, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        color = cs.surface.copy(alpha = 0.94f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                tint = cs.primary,
                modifier = Modifier
                    .padding(top = 2.dp)
                    .size(18.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = stringResource(R.string.offline_engine_banner_title),
                    style = MaterialTheme.typography.labelLarge,
                    color = cs.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = stringResource(R.string.offline_engine_banner_desc),
                    style = MaterialTheme.typography.bodySmall,
                    color = cs.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun CreatorInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    testTag: String,
    minLines: Int = 1,
    maxLines: Int = 3,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = cs.onSecondaryContainer,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyMedium,
                    color = cs.onTertiaryContainer
                )
            },
            trailingIcon = {
                if (value.isNotEmpty()) {
                    IconButton(
                        onClick = { onValueChange("") },
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("${testTag}_clear")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.action_clear_input),
                            tint = cs.onSurfaceVariant
                        )
                    }
                }
            },
            minLines = minLines,
            maxLines = maxLines,
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = cs.onSurface),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = cs.surface,
                unfocusedContainerColor = cs.surface,
                focusedBorderColor = cs.primary,
                unfocusedBorderColor = cs.outline,
                cursorColor = cs.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag(testTag)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuickPresetRow(
    presets: List<String>,
    onPresetSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.quick_examples_label),
            style = MaterialTheme.typography.labelMedium,
            color = cs.onTertiaryContainer,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            presets.forEachIndexed { idx, preset ->
                Surface(
                    shape = RoundedCornerShape(50),
                    color = cs.secondaryContainer,
                    border = BorderStroke(1.dp, cs.outline),
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .clip(RoundedCornerShape(50))
                        .clickable { onPresetSelected(preset) }
                        .testTag("preset_chip_$idx")
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = preset,
                            style = MaterialTheme.typography.labelMedium,
                            color = cs.onSecondaryContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NeonPrimaryButton(
    text: String,
    onClick: () -> Unit,
    testTag: String,
    neonGlowEnabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val gradient = Brush.horizontalGradient(
        colors = listOf(cs.primaryContainer, cs.primary, cs.secondary)
    )

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = cs.onPrimaryContainer
        ),
        contentPadding = PaddingValues(0.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .then(
                if (neonGlowEnabled) {
                    Modifier.drawBehind {
                        drawRoundRect(
                            brush = Brush.horizontalGradient(
                                listOf(
                                    cs.primaryContainer.copy(alpha = 0.35f),
                                    cs.primary.copy(alpha = 0.35f)
                                )
                            ),
                            topLeft = Offset(0f, 4.dp.toPx()),
                            size = size,
                            cornerRadius = CornerRadius(18.dp.toPx(), 18.dp.toPx())
                        )
                    }
                } else {
                    Modifier
                }
            )
            .background(gradient, RoundedCornerShape(16.dp))
            .testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = cs.onPrimaryContainer,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = cs.onPrimaryContainer
            )
        }
    }
}

@Composable
fun CopyActionButton(
    isCopied: Boolean,
    onClick: () -> Unit,
    testTag: String,
    labelOverride: String? = null,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val containerColor by animateColorAsState(
        targetValue = if (isCopied) SuccessMint.copy(alpha = 0.16f) else cs.secondaryContainer,
        label = "copyBtnBg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isCopied) SuccessMint else cs.outlineVariant.copy(alpha = 0.6f),
        label = "copyBtnBorder"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isCopied) SuccessMint else cs.onSecondaryContainer,
        label = "copyBtnContent"
    )

    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, borderColor),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
        modifier = modifier
            .minimumInteractiveComponentSize()
            .testTag(testTag)
    ) {
        Icon(
            imageVector = if (isCopied) Icons.Default.Check else Icons.Default.ContentCopy,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = if (isCopied) {
                stringResource(R.string.action_copied)
            } else {
                labelOverride ?: stringResource(R.string.action_copy)
            },
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun ResultGlowCard(
    badgeText: String,
    neonGlowEnabled: Boolean,
    modifier: Modifier = Modifier,
    headerTrailing: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val borderColor = if (neonGlowEnabled) {
        cs.outlineVariant.copy(alpha = 0.5f)
    } else {
        cs.outline
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cs.surfaceVariant),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = cs.primaryContainer.copy(alpha = 0.24f),
                    border = BorderStroke(1.dp, cs.primary.copy(alpha = 0.45f))
                ) {
                    Text(
                        text = badgeText.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = cs.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
                headerTrailing?.invoke()
            }
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
fun EmptyToolPlaceholder(
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cs.surface),
        border = BorderStroke(1.dp, cs.outline)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(cs.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = cs.primary,
                    modifier = Modifier.size(26.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.empty_tool_state_title),
                style = MaterialTheme.typography.titleMedium,
                color = cs.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.empty_tool_state_desc),
                style = MaterialTheme.typography.bodySmall,
                color = cs.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}
