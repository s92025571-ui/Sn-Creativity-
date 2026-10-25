package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CreatorTemplateEngine
import com.example.data.ThemePreferencesRepository
import com.example.model.BottomNavTab
import com.example.model.CopiedItemRecord
import com.example.model.CreatorToolType
import com.example.model.DarkThemeVariant
import com.example.model.HashtagResult
import com.example.model.ScriptLength
import com.example.model.ScriptResult
import com.example.model.ThumbnailAspectRatio
import com.example.model.ThumbnailPromptItem
import com.example.model.TitleSuggestion
import com.example.model.VideoIdeaItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CreatorUiState(
    val showSplash: Boolean = true,
    val selectedTab: BottomNavTab = BottomNavTab.HOME,
    val activeTool: CreatorToolType? = null,

    // Theme & Preferences
    val darkThemeVariant: DarkThemeVariant = DarkThemeVariant.OBSIDIAN_PURPLE,
    val neonGlowEnabled: Boolean = true,
    val lowercaseHashtags: Boolean = false,
    val includeScriptCues: Boolean = true,

    // 1. Gaming Titles
    val gamingTitlesInput: String = "",
    val gamingTitlesResults: List<TitleSuggestion> = emptyList(),
    val gamingTitlesSeed: Int = 0,

    // 2. Hashtag Generator
    val hashtagInput: String = "",
    val hashtagResult: HashtagResult? = null,

    // 3. Voice-over & Scripts
    val scriptDetailsInput: String = "",
    val selectedScriptLength: ScriptLength = ScriptLength.SHORT,
    val scriptResult: ScriptResult? = null,

    // 4. Thumbnail Prompts
    val thumbnailInput: String = "",
    val selectedAspectRatio: ThumbnailAspectRatio = ThumbnailAspectRatio.WIDESCREEN_16_9,
    val thumbnailResults: List<ThumbnailPromptItem> = emptyList(),
    val thumbnailSeed: Int = 0,

    // 5. Video Ideas
    val videoIdeasInput: String = "",
    val videoIdeasResults: List<VideoIdeaItem> = emptyList(),
    val videoIdeasSeed: Int = 0,

    // Session clipboard history & UI feedback
    val recentCopiedItems: List<CopiedItemRecord> = emptyList(),
    val lastCopiedId: String? = null,
    val validationMessage: String? = null
)

class CreatorViewModel(application: Application) : AndroidViewModel(application) {

    private val preferencesRepository = ThemePreferencesRepository(application.applicationContext)

    private val _uiState = MutableStateFlow(CreatorUiState())
    val uiState: StateFlow<CreatorUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            preferencesRepository.preferencesFlow.collectLatest { stored ->
                _uiState.update { state ->
                    val updatedHashtags = if (
                        state.lowercaseHashtags != stored.lowercaseHashtags &&
                        state.hashtagResult != null &&
                        state.hashtagInput.isNotBlank()
                    ) {
                        CreatorTemplateEngine.buildHashtags(state.hashtagInput, stored.lowercaseHashtags)
                    } else {
                        state.hashtagResult
                    }

                    val updatedScript = if (
                        state.includeScriptCues != stored.includeScriptCues &&
                        state.scriptResult != null &&
                        state.scriptDetailsInput.isNotBlank()
                    ) {
                        CreatorTemplateEngine.buildVoiceoverScript(
                            rawDetails = state.scriptDetailsInput,
                            length = state.selectedScriptLength,
                            includeCues = stored.includeScriptCues
                        )
                    } else {
                        state.scriptResult
                    }

                    state.copy(
                        darkThemeVariant = stored.darkThemeVariant,
                        neonGlowEnabled = stored.neonGlowEnabled,
                        lowercaseHashtags = stored.lowercaseHashtags,
                        includeScriptCues = stored.includeScriptCues,
                        hashtagResult = updatedHashtags,
                        scriptResult = updatedScript
                    )
                }
            }
        }
    }

    fun finishSplash() {
        _uiState.update { it.copy(showSplash = false) }
    }

    fun selectTab(tab: BottomNavTab) {
        _uiState.update {
            it.copy(
                selectedTab = tab,
                activeTool = null,
                validationMessage = null
            )
        }
    }

    fun openTool(tool: CreatorToolType) {
        _uiState.update {
            it.copy(
                activeTool = tool,
                validationMessage = null
            )
        }
    }

    fun navigateBackFromTool() {
        _uiState.update {
            it.copy(
                activeTool = null,
                validationMessage = null
            )
        }
    }

    fun clearValidationMessage() {
        _uiState.update { it.copy(validationMessage = null) }
    }

    fun selectDarkThemeVariant(variant: DarkThemeVariant) {
        _uiState.update { it.copy(darkThemeVariant = variant) }
        viewModelScope.launch {
            preferencesRepository.setDarkThemeVariant(variant)
        }
    }

    // --- 1. Gaming Titles ---
    fun updateGamingTitlesInput(value: String) {
        _uiState.update { it.copy(gamingTitlesInput = value, validationMessage = null) }
    }

    fun generateGamingTitles(emptyError: String) {
        val current = _uiState.value
        if (current.gamingTitlesInput.isBlank()) {
            _uiState.update { it.copy(validationMessage = emptyError) }
            return
        }
        val nextSeed = current.gamingTitlesSeed + 1
        val suggestions = CreatorTemplateEngine.buildGamingTitles(
            rawTopic = current.gamingTitlesInput,
            seedOffset = current.gamingTitlesSeed
        )
        _uiState.update {
            it.copy(
                gamingTitlesResults = suggestions,
                gamingTitlesSeed = nextSeed,
                validationMessage = null
            )
        }
    }

    // --- 2. Hashtag Generator ---
    fun updateHashtagInput(value: String) {
        _uiState.update { it.copy(hashtagInput = value, validationMessage = null) }
    }

    fun generateHashtags(emptyError: String) {
        val current = _uiState.value
        if (current.hashtagInput.isBlank()) {
            _uiState.update { it.copy(validationMessage = emptyError) }
            return
        }
        val tags = CreatorTemplateEngine.buildHashtags(
            rawTopic = current.hashtagInput,
            lowercaseOnly = current.lowercaseHashtags
        )
        _uiState.update {
            it.copy(
                hashtagResult = tags,
                validationMessage = null
            )
        }
    }

    // --- 3. Voice-over & Scripts ---
    fun updateScriptDetailsInput(value: String) {
        _uiState.update { it.copy(scriptDetailsInput = value, validationMessage = null) }
    }

    fun updateScriptLength(length: ScriptLength) {
        _uiState.update { state ->
            val updatedScript = if (state.scriptResult != null && state.scriptDetailsInput.isNotBlank()) {
                CreatorTemplateEngine.buildVoiceoverScript(
                    rawDetails = state.scriptDetailsInput,
                    length = length,
                    includeCues = state.includeScriptCues
                )
            } else {
                state.scriptResult
            }
            state.copy(
                selectedScriptLength = length,
                scriptResult = updatedScript
            )
        }
    }

    fun generateScript(emptyError: String) {
        val current = _uiState.value
        if (current.scriptDetailsInput.isBlank()) {
            _uiState.update { it.copy(validationMessage = emptyError) }
            return
        }
        val script = CreatorTemplateEngine.buildVoiceoverScript(
            rawDetails = current.scriptDetailsInput,
            length = current.selectedScriptLength,
            includeCues = current.includeScriptCues
        )
        _uiState.update {
            it.copy(
                scriptResult = script,
                validationMessage = null
            )
        }
    }

    // --- 4. Thumbnail Prompts ---
    fun updateThumbnailInput(value: String) {
        _uiState.update { it.copy(thumbnailInput = value, validationMessage = null) }
    }

    fun updateAspectRatio(ratio: ThumbnailAspectRatio) {
        _uiState.update { state ->
            val updatedPrompts = if (state.thumbnailResults.isNotEmpty() && state.thumbnailInput.isNotBlank()) {
                CreatorTemplateEngine.buildThumbnailPrompts(
                    rawIdea = state.thumbnailInput,
                    aspectRatio = ratio,
                    seedOffset = (state.thumbnailSeed - 1).coerceAtLeast(0)
                )
            } else {
                state.thumbnailResults
            }
            state.copy(
                selectedAspectRatio = ratio,
                thumbnailResults = updatedPrompts
            )
        }
    }

    fun generateThumbnailPrompts(emptyError: String) {
        val current = _uiState.value
        if (current.thumbnailInput.isBlank()) {
            _uiState.update { it.copy(validationMessage = emptyError) }
            return
        }
        val nextSeed = current.thumbnailSeed + 1
        val prompts = CreatorTemplateEngine.buildThumbnailPrompts(
            rawIdea = current.thumbnailInput,
            aspectRatio = current.selectedAspectRatio,
            seedOffset = current.thumbnailSeed
        )
        _uiState.update {
            it.copy(
                thumbnailResults = prompts,
                thumbnailSeed = nextSeed,
                validationMessage = null
            )
        }
    }

    // --- 5. Video Ideas ---
    fun updateVideoIdeasInput(value: String) {
        _uiState.update { it.copy(videoIdeasInput = value, validationMessage = null) }
    }

    fun generateVideoIdeas(emptyError: String) {
        val current = _uiState.value
        if (current.videoIdeasInput.isBlank()) {
            _uiState.update { it.copy(validationMessage = emptyError) }
            return
        }
        val nextSeed = current.videoIdeasSeed + 1
        val ideas = CreatorTemplateEngine.buildVideoIdeas(
            rawNiche = current.videoIdeasInput,
            seedOffset = current.videoIdeasSeed
        )
        _uiState.update {
            it.copy(
                videoIdeasResults = ideas,
                videoIdeasSeed = nextSeed,
                validationMessage = null
            )
        }
    }

    // --- Copy & Preferences ---
    fun recordCopiedItem(itemId: String, toolName: String, content: String) {
        val newRecord = CopiedItemRecord(
            id = "${itemId}_${System.currentTimeMillis()}",
            toolName = toolName,
            content = content
        )
        _uiState.update { state ->
            val updatedHistory = (listOf(newRecord) + state.recentCopiedItems.filterNot { it.content == content })
                .take(15)
            state.copy(
                lastCopiedId = itemId,
                recentCopiedItems = updatedHistory
            )
        }
    }

    fun clearRecentCopies() {
        _uiState.update { it.copy(recentCopiedItems = emptyList(), lastCopiedId = null) }
    }

    fun toggleNeonGlow(enabled: Boolean) {
        _uiState.update { it.copy(neonGlowEnabled = enabled) }
        viewModelScope.launch {
            preferencesRepository.setNeonGlowEnabled(enabled)
        }
    }

    fun toggleLowercaseHashtags(enabled: Boolean) {
        _uiState.update { state ->
            val updatedHashtags = if (state.hashtagResult != null && state.hashtagInput.isNotBlank()) {
                CreatorTemplateEngine.buildHashtags(state.hashtagInput, enabled)
            } else {
                state.hashtagResult
            }
            state.copy(
                lowercaseHashtags = enabled,
                hashtagResult = updatedHashtags
            )
        }
        viewModelScope.launch {
            preferencesRepository.setLowercaseHashtags(enabled)
        }
    }

    fun toggleIncludeScriptCues(enabled: Boolean) {
        _uiState.update { state ->
            val updatedScript = if (state.scriptResult != null && state.scriptDetailsInput.isNotBlank()) {
                CreatorTemplateEngine.buildVoiceoverScript(
                    rawDetails = state.scriptDetailsInput,
                    length = state.selectedScriptLength,
                    includeCues = enabled
                )
            } else {
                state.scriptResult
            }
            state.copy(
                includeScriptCues = enabled,
                scriptResult = updatedScript
            )
        }
        viewModelScope.launch {
            preferencesRepository.setIncludeScriptCues(enabled)
        }
    }

    fun resetAllWorkspace() {
        _uiState.update { state ->
            state.copy(
                gamingTitlesInput = "",
                gamingTitlesResults = emptyList(),
                hashtagInput = "",
                hashtagResult = null,
                scriptDetailsInput = "",
                scriptResult = null,
                thumbnailInput = "",
                thumbnailResults = emptyList(),
                videoIdeasInput = "",
                videoIdeasResults = emptyList(),
                recentCopiedItems = emptyList(),
                lastCopiedId = null,
                validationMessage = null
            )
        }
    }
}
