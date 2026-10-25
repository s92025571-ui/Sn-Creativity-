package com.example.data

import com.example.model.HashtagResult
import com.example.model.ScriptLength
import com.example.model.ScriptResult
import com.example.model.ThumbnailAspectRatio
import com.example.model.ThumbnailPromptItem
import com.example.model.TitleSuggestion
import com.example.model.VideoIdeaItem
import java.util.Locale

/**
 * Local deterministic template engine for SN Creativity.
 * Does not pretend to be a cloud AI service — builds structured, creator-ready
 * copy using proven gaming & creator formulas tailored to the user's input.
 */
object CreatorTemplateEngine {

    private fun cleanTopic(raw: String): String {
        return raw.trim().replace(Regex("\\s+"), " ")
    }

    private fun toTitleCaseWords(text: String): String {
        return text.split(" ").joinToString(" ") { word ->
            if (word.length <= 2 && word == word.uppercase(Locale.ROOT)) {
                word
            } else {
                word.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
                }
            }
        }
    }

    fun buildGamingTitles(rawTopic: String, seedOffset: Int = 0): List<TitleSuggestion> {
        val topic = toTitleCaseWords(cleanTopic(rawTopic))
        if (topic.isEmpty()) return emptyList()

        val templates = listOf(
            "High-CTR Hook" to "I Tried $topic For 24 Hours Straight — Here's What Happened",
            "Challenge" to "Can You Actually Beat $topic Without Taking A Single Hit?",
            "Pro Guide" to "7 Secret $topic Tricks Pros Never Tell Beginners",
            "Story Arc" to "How One Tiny Mistake In $topic Changed Everything",
            "Ranked / Meta" to "The New $topic Strategy Everyone Is Sleeping On",
            "Zero To Hero" to "Starting From Scratch In $topic Until I Reach Top Rank",
            "Mythbusting" to "Testing The Craziest Myths In $topic (Surprising Results)",
            "Clutch Highlight" to "The Most Intense Final Minute In $topic History"
        )

        val rotated = templates.drop(seedOffset % templates.size) +
            templates.take(seedOffset % templates.size)

        return rotated.take(6).mapIndexed { index, (badge, text) ->
            TitleSuggestion(
                id = "title_${seedOffset}_$index",
                styleBadge = badge,
                title = text
            )
        }
    }

    fun buildHashtags(rawTopic: String, lowercaseOnly: Boolean): HashtagResult {
        val cleaned = cleanTopic(rawTopic)
        if (cleaned.isEmpty()) {
            return HashtagResult(emptyList(), emptyList(), "")
        }

        val words = cleaned
            .replace(Regex("[^A-Za-z0-9 ]"), " ")
            .trim()
            .split(Regex("\\s+"))
            .filter { it.isNotBlank() }

        val pascalJoined = words.joinToString("") { word ->
            word.lowercase(Locale.ROOT).replaceFirstChar { it.titlecase(Locale.ROOT) }
        }
        val firstWord = words.firstOrNull()?.lowercase(Locale.ROOT)
            ?.replaceFirstChar { it.titlecase(Locale.ROOT) } ?: pascalJoined

        val rawPrimary = buildList {
            if (pascalJoined.isNotEmpty()) {
                add("#$pascalJoined")
                add("#${pascalJoined}Gameplay")
                add("#${pascalJoined}Clips")
                add("#${pascalJoined}Tips")
                add("#${pascalJoined}Community")
                add("#${pascalJoined}Highlights")
            }
            if (words.size > 1 && firstWord.length > 2) {
                add("#$firstWord")
                add("#${firstWord}Gaming")
            }
        }.distinct()

        val rawDiscovery = listOf(
            "#Gaming",
            "#GamingCommunity",
            "#GamerLife",
            "#GameplayMoments",
            "#ContentCreator",
            "#GamingSetup",
            "#StreamHighlights",
            "#GamingClips",
            "#LetsPlay",
            "#VideoGames"
        )

        val formatTag: (String) -> String = { tag ->
            if (lowercaseOnly) tag.lowercase(Locale.ROOT) else tag
        }

        val primary = rawPrimary.map(formatTag)
        val discovery = rawDiscovery.map(formatTag)
        val combined = (primary + discovery).distinct()

        return HashtagResult(
            primaryTags = primary,
            discoveryTags = discovery,
            allFormattedBlock = combined.joinToString(" ")
        )
    }

    fun buildVoiceoverScript(
        rawDetails: String,
        length: ScriptLength,
        includeCues: Boolean
    ): ScriptResult? {
        val details = cleanTopic(rawDetails)
        if (details.isEmpty()) return null

        val headlineTopic = toTitleCaseWords(details)

        val cue: (String) -> String = { label ->
            if (includeCues) "[$label]\n" else ""
        }

        val (durationLabel, body) = when (length) {
            ScriptLength.SHORT -> {
                "~35–45 Seconds (Shorts / Reels)" to buildString {
                    append(cue("0:00 - HOOK • Fast Cut"))
                    append("Nobody expected \"$details\" to actually work—until the final thirty seconds.\n\n")
                    append(cue("0:06 - SETUP • Crisp Gameplay"))
                    append("Here is the exact rule I set for this run: no shortcuts, no safe backups, and everything on the line.\n\n")
                    append(cue("0:18 - ESCALATION • Zoom & Sound Riser"))
                    append("At first it looked completely impossible, especially right when the pressure peaked. So I switched up my approach on the fly and went all-in on one risky play.\n\n")
                    append(cue("0:32 - PAYOFF & OUTRO"))
                    append("Watch how close that ending got. Drop a comment with what challenge I should try next!")
                }
            }

            ScriptLength.MEDIUM -> {
                "~2–3 Minutes (Focused Breakdown)" to buildString {
                    append(cue("INTRO HOOK • 0:00–0:20"))
                    append("Today we're diving straight into $details—and by the end of this video, you'll see the exactturning point that made the whole run worth it.\n\n")
                    append(cue("PART 1 • THE PLAN & LOADOUT"))
                    append("Before jumping in, I mapped out three priorities around $details: speed, positioning, and resource control. Most players rush the opening minute, so I deliberately played patient early.\n\n")
                    append(cue("PART 2 • THE TURNING POINT"))
                    append("Midway through, everything went sideways. Take a look at this sequence—one mistimed move almost ended the attempt, forcing an immediate adjustment.\n\n")
                    append(cue("PART 3 • FINAL CLUTCH & TAKEAWAY"))
                    append("Once the rhythm clicked, the final stretch came together cleanly. If you're trying $details yourself, focus on timing your engagements rather than forcing early plays.\n\n")
                    append(cue("OUTRO CTA"))
                    append("If you enjoyed this breakdown, let me know what variation of $details you want tested next time.")
                }
            }

            ScriptLength.LONG -> {
                "~8–10 Minutes (Full Story & Guide)" to buildString {
                    append(cue("COLD OPEN • 0:00–0:35"))
                    append("This single moment in $details took hours of preparation—and it nearly fell apart in five seconds. Let's rewind to the very beginning so you can see how we got here.\n\n")
                    append(cue("CHAPTER 1 • THE CHALLENGE RULES"))
                    append("For this episode on $details, I wanted to push past standard runs. That meant strict rules from minute one, zero resets on minor mistakes, and adapting to whatever happened live.\n\n")
                    append(cue("CHAPTER 2 • EARLY GAME MOMENTUM"))
                    append("The first phase was all about building a foundation. Notice how small decisions here—saving cooldowns, picking clean angles, and tracking objectives—set up everything later.\n\n")
                    append(cue("CHAPTER 3 • MID-GAME CRISIS (B-ROLL MONTAGE)"))
                    append("Right around the halfway mark, the difficulty spiked hard. Every safe option disappeared, and we had to improvise a completely new strategy under pressure.\n\n")
                    append(cue("CHAPTER 4 • KEY LESSONS & PRO TIPS"))
                    append("Looking back at the footage from $details, three habits made the biggest difference:\n")
                    append("1. Staying calm when health or resources drop low.\n")
                    append("2. Reading opponent patterns before committing.\n")
                    append("3. Saving your strongest utility for the decisive objective.\n\n")
                    append(cue("CHAPTER 5 • THE FINALE & OUTRO"))
                    append("That brings us back to the final showdown. Watch the full sequence play out—and tell me in the comments how you would have handled $details!")
                }
            }
        }

        return ScriptResult(
            length = length,
            estimatedDuration = durationLabel,
            title = "Script: $headlineTopic",
            fullScript = body
        )
    }

    fun buildThumbnailPrompts(
        rawIdea: String,
        aspectRatio: ThumbnailAspectRatio,
        seedOffset: Int = 0
    ): List<ThumbnailPromptItem> {
        val idea = cleanTopic(rawIdea)
        if (idea.isEmpty()) return emptyList()

        val compositionNote = when (aspectRatio) {
            ThumbnailAspectRatio.VERTICAL_9_16 ->
                "Vertical 9:16 portrait framing, centered focal subject in the middle third with clean dark headroom at top and bottom for Shorts/Reels title safe zones"
            ThumbnailAspectRatio.WIDESCREEN_16_9 ->
                "Widescreen 16:9 rule-of-thirds framing, expressive main subject positioned on the right two-thirds with negative space on the left for bold thumbnail text"
        }

        val variants = listOf(
            "High-Contrast Action" to
                "Cinematic gaming thumbnail of $idea, dramatic rim lighting in electric neon purple and fiery orange, intense motion blur debris, ultra-sharp foreground focus, deep shadows, $compositionNote, 8k digital render, vibrant color grading, --ar ${aspectRatio.ratioLabel}",
            "Clean 3D Studio" to
                "Stylized 3D render of $idea, glossy studio rim lights with glowing violet and cyan highlights, crisp silhouette separation against a dark atmospheric bokeh background, high micro-contrast, $compositionNote, Unreal Engine 5 aesthetic, --ar ${aspectRatio.ratioLabel}",
            "Dramatic Close-Up" to
                "Extreme close-up perspective featuring $idea, dramatic low-angle camera lens (24mm), volumetric fog illuminated by magenta and neon purple spotlights, crisp sparks in foreground, $compositionNote, hyper-detailed key art, --ar ${aspectRatio.ratioLabel}",
            "Minimalist Contrast" to
                "Bold graphic concept art of $idea, stark obsidian dark backdrop with a single vivid neon violet backlight halo, ultra-clean edges for instant mobile readability at small sizes, $compositionNote, sleek poster illustration, --ar ${aspectRatio.ratioLabel}"
        )

        val rotated = variants.drop(seedOffset % variants.size) +
            variants.take(seedOffset % variants.size)

        return rotated.take(3).mapIndexed { index, (badge, prompt) ->
            ThumbnailPromptItem(
                id = "thumb_${aspectRatio.name}_${seedOffset}_$index",
                styleBadge = badge,
                aspectRatio = aspectRatio,
                promptText = prompt
            )
        }
    }

    fun buildVideoIdeas(rawNiche: String, seedOffset: Int = 0): List<VideoIdeaItem> {
        val niche = toTitleCaseWords(cleanTopic(rawNiche))
        if (niche.isEmpty()) return emptyList()

        val concepts = listOf(
            Triple(
                "24-Hour Challenge",
                "100 Minutes vs. 100 Hours In $niche",
                "Compare your very first attempt against a refined strategy after intense practice, highlighting the 3 biggest breakthroughs on screen."
            ),
            Triple(
                "Constraint Run",
                "Winning $niche Using Only Starter Gear",
                "Lock out top-tier upgrades and narrate how positioning and smart fundamentals beat raw stats."
            ),
            Triple(
                "Tier & Meta Breakdown",
                "Ranking Every Popular Strategy In $niche From Overrated To Broken",
                "Test popular community loadouts side-by-side in real matches instead of just talking over a static chart."
            ),
            Triple(
                "Viewer Challenge",
                "Letting Wheel Spins Decide Every Move In $niche",
                "Add high-energy stakes where each milestone triggers a random rule change that forces live adaptation."
            ),
            Triple(
                "Mistake Analysis",
                "5 Habits Holding Players Back In $niche (And Instant Fixes)",
                "Show side-by-side clips of common positioning errors vs. the clean adjustment so viewers get immediate value."
            ),
            Triple(
                "Hidden Details",
                "10 Secret Mechanics & Details You Missed In $niche",
                "Fast-paced showcase of lesser-known interactions, shortcuts, and environmental tricks."
            )
        )

        val rotated = concepts.drop(seedOffset % concepts.size) +
            concepts.take(seedOffset % concepts.size)

        return rotated.take(5).mapIndexed { index, (badge, title, outline) ->
            VideoIdeaItem(
                id = "idea_${seedOffset}_$index",
                formatBadge = badge,
                hookTitle = title,
                conceptOutline = outline
            )
        }
    }
}
