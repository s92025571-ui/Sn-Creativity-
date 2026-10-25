package com.example.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.R
import com.example.model.BottomNavTab
import com.example.model.CreatorToolType
import com.example.ui.screens.CreateHubScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.GamingTitlesScreen
import com.example.ui.screens.HashtagGeneratorScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.ThumbnailPromptsScreen
import com.example.ui.screens.VideoIdeasScreen
import com.example.ui.screens.VoiceoverScriptsScreen
import com.example.ui.theme.SnCreativityTheme
import com.example.viewmodel.CreatorViewModel
import kotlinx.coroutines.launch

private data class NavTabInfo(
    val tab: BottomNavTab,
    val labelRes: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

@Composable
fun SnCreativityApp(
    viewModel: CreatorViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SnCreativityTheme(themeVariant = uiState.darkThemeVariant) {
        val cs = MaterialTheme.colorScheme
        val context = LocalContext.current
        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()

        val copiedMsg = stringResource(R.string.msg_copied_clipboard)
        val emptyTopicMsg = stringResource(R.string.msg_enter_topic_error)
        val workspaceClearedMsg = stringResource(R.string.pref_workspace_cleared)

        val toolGamingTitleName = stringResource(R.string.tool_gaming_titles)
        val toolHashtagName = stringResource(R.string.tool_hashtag_generator)
        val toolVoiceoverName = stringResource(R.string.tool_voiceover_scripts)
        val toolThumbnailName = stringResource(R.string.tool_thumbnail_prompts)
        val toolVideoIdeasName = stringResource(R.string.tool_video_ideas)

        LaunchedEffect(uiState.validationMessage) {
            val msg = uiState.validationMessage
            if (!msg.isNullOrBlank()) {
                snackbarHostState.showSnackbar(
                    message = msg,
                    duration = SnackbarDuration.Short
                )
                viewModel.clearValidationMessage()
            }
        }

        val copyToClipboard: (String, String, String) -> Unit = { id, toolLabel, text ->
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
            clipboard?.setPrimaryClip(ClipData.newPlainText("SN Creativity", text))
            viewModel.recordCopiedItem(id, toolLabel, text)
            coroutineScope.launch {
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(
                    message = copiedMsg,
                    duration = SnackbarDuration.Short
                )
            }
        }

        Crossfade(
            targetState = uiState.showSplash,
            animationSpec = tween(durationMillis = 360),
            label = "rootSplashCrossfade"
        ) { isSplash ->
            if (isSplash) {
                SplashScreen(onSplashFinished = { viewModel.finishSplash() })
            } else {
                if (uiState.activeTool == null && uiState.selectedTab != BottomNavTab.HOME) {
                    BackHandler {
                        viewModel.selectTab(BottomNavTab.HOME)
                    }
                }

                val navItems = remember {
                    listOf(
                        NavTabInfo(
                            tab = BottomNavTab.HOME,
                            labelRes = R.string.nav_home,
                            selectedIcon = Icons.Filled.Home,
                            unselectedIcon = Icons.Outlined.Home,
                            testTag = "nav_tab_home"
                        ),
                        NavTabInfo(
                            tab = BottomNavTab.CREATE,
                            labelRes = R.string.nav_create,
                            selectedIcon = Icons.Filled.AutoAwesome,
                            unselectedIcon = Icons.Outlined.AutoAwesome,
                            testTag = "nav_tab_create"
                        ),
                        NavTabInfo(
                            tab = BottomNavTab.SETTINGS,
                            labelRes = R.string.nav_settings,
                            selectedIcon = Icons.Filled.Settings,
                            unselectedIcon = Icons.Outlined.Settings,
                            testTag = "nav_tab_settings"
                        )
                    )
                }

                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                    val useNavigationRail = maxWidth >= 720.dp

                    Row(modifier = Modifier.fillMaxSize()) {
                        if (useNavigationRail) {
                            NavigationRail(
                                containerColor = cs.surface,
                                contentColor = cs.onSurface
                            ) {
                                navItems.forEach { item ->
                                    val isSelected =
                                        uiState.activeTool == null && uiState.selectedTab == item.tab
                                    NavigationRailItem(
                                        selected = isSelected,
                                        onClick = { viewModel.selectTab(item.tab) },
                                        icon = {
                                            Icon(
                                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                                contentDescription = stringResource(item.labelRes)
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = stringResource(item.labelRes),
                                                style = MaterialTheme.typography.labelMedium
                                            )
                                        },
                                        colors = NavigationRailItemDefaults.colors(
                                            selectedIconColor = cs.onPrimaryContainer,
                                            selectedTextColor = cs.onSecondaryContainer,
                                            indicatorColor = cs.primaryContainer,
                                            unselectedIconColor = cs.onTertiaryContainer,
                                            unselectedTextColor = cs.onTertiaryContainer
                                        ),
                                        modifier = Modifier.testTag(item.testTag)
                                    )
                                }
                            }
                        }

                        Scaffold(
                            modifier = Modifier.weight(1f),
                            containerColor = cs.background,
                            contentWindowInsets = WindowInsets.safeDrawing,
                            snackbarHost = {
                                SnackbarHost(hostState = snackbarHostState) { data ->
                                    Snackbar(
                                        snackbarData = data,
                                        containerColor = cs.surfaceVariant,
                                        contentColor = cs.onSurface,
                                        actionColor = cs.primary
                                    )
                                }
                            },
                            bottomBar = {
                                if (!useNavigationRail) {
                                    Box(modifier = Modifier.fillMaxWidth()) {
                                        NavigationBar(
                                            containerColor = cs.surface,
                                            tonalElevation = 0.dp
                                        ) {
                                            navItems.forEach { item ->
                                                val isSelected =
                                                    uiState.activeTool == null && uiState.selectedTab == item.tab
                                                NavigationBarItem(
                                                    selected = isSelected,
                                                    onClick = { viewModel.selectTab(item.tab) },
                                                    icon = {
                                                        Icon(
                                                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                                            contentDescription = stringResource(item.labelRes)
                                                        )
                                                    },
                                                    label = {
                                                        Text(
                                                            text = stringResource(item.labelRes),
                                                            style = MaterialTheme.typography.labelMedium
                                                        )
                                                    },
                                                    colors = NavigationBarItemDefaults.colors(
                                                        selectedIconColor = cs.onPrimaryContainer,
                                                        selectedTextColor = cs.onSecondaryContainer,
                                                        indicatorColor = cs.primaryContainer,
                                                        unselectedIconColor = cs.onTertiaryContainer,
                                                        unselectedTextColor = cs.onTertiaryContainer
                                                    ),
                                                    modifier = Modifier.testTag(item.testTag)
                                                )
                                            }
                                        }
                                        HorizontalDivider(
                                            color = cs.outline,
                                            thickness = 1.dp
                                        )
                                    }
                                }
                            }
                        ) { innerPadding ->
                            val activeTool = uiState.activeTool
                            if (activeTool != null) {
                                when (activeTool) {
                                    CreatorToolType.GAMING_TITLES -> {
                                        GamingTitlesScreen(
                                            input = uiState.gamingTitlesInput,
                                            results = uiState.gamingTitlesResults,
                                            neonGlowEnabled = uiState.neonGlowEnabled,
                                            lastCopiedId = uiState.lastCopiedId,
                                            onInputChange = viewModel::updateGamingTitlesInput,
                                            onGenerate = { viewModel.generateGamingTitles(emptyTopicMsg) },
                                            onCopyItem = { id, text ->
                                                copyToClipboard(id, toolGamingTitleName, text)
                                            },
                                            onBack = viewModel::navigateBackFromTool,
                                            contentPadding = innerPadding
                                        )
                                    }

                                    CreatorToolType.HASHTAG_GENERATOR -> {
                                        HashtagGeneratorScreen(
                                            input = uiState.hashtagInput,
                                            result = uiState.hashtagResult,
                                            neonGlowEnabled = uiState.neonGlowEnabled,
                                            lastCopiedId = uiState.lastCopiedId,
                                            onInputChange = viewModel::updateHashtagInput,
                                            onGenerate = { viewModel.generateHashtags(emptyTopicMsg) },
                                            onCopyItem = { id, text ->
                                                copyToClipboard(id, toolHashtagName, text)
                                            },
                                            onBack = viewModel::navigateBackFromTool,
                                            contentPadding = innerPadding
                                        )
                                    }

                                    CreatorToolType.VOICEOVER_SCRIPTS -> {
                                        VoiceoverScriptsScreen(
                                            input = uiState.scriptDetailsInput,
                                            selectedLength = uiState.selectedScriptLength,
                                            scriptResult = uiState.scriptResult,
                                            neonGlowEnabled = uiState.neonGlowEnabled,
                                            lastCopiedId = uiState.lastCopiedId,
                                            onInputChange = viewModel::updateScriptDetailsInput,
                                            onLengthSelect = viewModel::updateScriptLength,
                                            onGenerate = { viewModel.generateScript(emptyTopicMsg) },
                                            onCopyItem = { id, text ->
                                                copyToClipboard(id, toolVoiceoverName, text)
                                            },
                                            onBack = viewModel::navigateBackFromTool,
                                            contentPadding = innerPadding
                                        )
                                    }

                                    CreatorToolType.THUMBNAIL_PROMPTS -> {
                                        ThumbnailPromptsScreen(
                                            input = uiState.thumbnailInput,
                                            selectedRatio = uiState.selectedAspectRatio,
                                            results = uiState.thumbnailResults,
                                            neonGlowEnabled = uiState.neonGlowEnabled,
                                            lastCopiedId = uiState.lastCopiedId,
                                            onInputChange = viewModel::updateThumbnailInput,
                                            onRatioSelect = viewModel::updateAspectRatio,
                                            onGenerate = { viewModel.generateThumbnailPrompts(emptyTopicMsg) },
                                            onCopyItem = { id, text ->
                                                copyToClipboard(id, toolThumbnailName, text)
                                            },
                                            onBack = viewModel::navigateBackFromTool,
                                            contentPadding = innerPadding
                                        )
                                    }

                                    CreatorToolType.VIDEO_IDEAS -> {
                                        VideoIdeasScreen(
                                            input = uiState.videoIdeasInput,
                                            results = uiState.videoIdeasResults,
                                            neonGlowEnabled = uiState.neonGlowEnabled,
                                            lastCopiedId = uiState.lastCopiedId,
                                            onInputChange = viewModel::updateVideoIdeasInput,
                                            onGenerate = { viewModel.generateVideoIdeas(emptyTopicMsg) },
                                            onCopyItem = { id, text ->
                                                copyToClipboard(id, toolVideoIdeasName, text)
                                            },
                                            onBack = viewModel::navigateBackFromTool,
                                            contentPadding = innerPadding
                                        )
                                    }
                                }
                            } else {
                                when (uiState.selectedTab) {
                                    BottomNavTab.HOME -> {
                                        DashboardScreen(
                                            neonGlowEnabled = uiState.neonGlowEnabled,
                                            onOpenTool = viewModel::openTool,
                                            contentPadding = innerPadding
                                        )
                                    }

                                    BottomNavTab.CREATE -> {
                                        CreateHubScreen(
                                            neonGlowEnabled = uiState.neonGlowEnabled,
                                            recentCopiedItems = uiState.recentCopiedItems,
                                            lastCopiedId = uiState.lastCopiedId,
                                            onOpenTool = viewModel::openTool,
                                            onCopyText = copyToClipboard,
                                            onClearRecent = viewModel::clearRecentCopies,
                                            contentPadding = innerPadding
                                        )
                                    }

                                    BottomNavTab.SETTINGS -> {
                                        SettingsScreen(
                                            selectedThemeVariant = uiState.darkThemeVariant,
                                            neonGlowEnabled = uiState.neonGlowEnabled,
                                            lowercaseHashtags = uiState.lowercaseHashtags,
                                            includeScriptCues = uiState.includeScriptCues,
                                            onSelectThemeVariant = viewModel::selectDarkThemeVariant,
                                            onToggleNeonGlow = viewModel::toggleNeonGlow,
                                            onToggleLowercaseHashtags = viewModel::toggleLowercaseHashtags,
                                            onToggleIncludeScriptCues = viewModel::toggleIncludeScriptCues,
                                            onResetWorkspace = {
                                                viewModel.resetAllWorkspace()
                                                coroutineScope.launch {
                                                    snackbarHostState.showSnackbar(workspaceClearedMsg)
                                                }
                                            },
                                            contentPadding = innerPadding
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
