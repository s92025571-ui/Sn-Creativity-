package com.example.model

enum class CreatorToolType(
    val id: String
) {
    GAMING_TITLES("gaming_titles"),
    HASHTAG_GENERATOR("hashtag_generator"),
    VOICEOVER_SCRIPTS("voiceover_scripts"),
    THUMBNAIL_PROMPTS("thumbnail_prompts"),
    VIDEO_IDEAS("video_ideas")
}

enum class BottomNavTab {
    HOME,
    CREATE,
    SETTINGS
}

enum class DarkThemeVariant(val storageKey: String) {
    OBSIDIAN_PURPLE("obsidian_purple"),
    MIDNIGHT_BLUE("midnight_blue"),
    DEEP_CHARCOAL("deep_charcoal");

    companion object {
        fun fromKey(key: String?): DarkThemeVariant {
            return entries.firstOrNull { it.storageKey == key } ?: OBSIDIAN_PURPLE
        }
    }
}

enum class ScriptLength {
    SHORT,
    MEDIUM,
    LONG
}

enum class ThumbnailAspectRatio(val ratioLabel: String) {
    VERTICAL_9_16("9:16"),
    WIDESCREEN_16_9("16:9")
}

data class TitleSuggestion(
    val id: String,
    val styleBadge: String,
    val title: String
)

data class HashtagResult(
    val primaryTags: List<String>,
    val discoveryTags: List<String>,
    val allFormattedBlock: String
)

data class ScriptResult(
    val length: ScriptLength,
    val estimatedDuration: String,
    val title: String,
    val fullScript: String
)

data class ThumbnailPromptItem(
    val id: String,
    val styleBadge: String,
    val aspectRatio: ThumbnailAspectRatio,
    val promptText: String
)

data class VideoIdeaItem(
    val id: String,
    val formatBadge: String,
    val hookTitle: String,
    val conceptOutline: String
)

data class CopiedItemRecord(
    val id: String,
    val toolName: String,
    val content: String
)
