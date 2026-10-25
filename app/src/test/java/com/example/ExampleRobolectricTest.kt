package com.example

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.CreatorTemplateEngine
import com.example.model.DarkThemeVariant
import com.example.model.ScriptLength
import com.example.model.ThumbnailAspectRatio
import com.example.ui.theme.CharcoalBackground
import com.example.ui.theme.MidnightBackground
import com.example.ui.theme.ObsidianBackground
import com.example.ui.theme.colorSchemeForVariant
import com.example.viewmodel.CreatorViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `app_name is SN Creativity`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("SN Creativity", appName)
    }

    @Test
    fun `dark theme variations switch color schemes and update ViewModel state`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = CreatorViewModel(app)

        assertEquals(DarkThemeVariant.OBSIDIAN_PURPLE, viewModel.uiState.value.darkThemeVariant)
        assertEquals(ObsidianBackground, colorSchemeForVariant(DarkThemeVariant.OBSIDIAN_PURPLE).background)

        viewModel.selectDarkThemeVariant(DarkThemeVariant.MIDNIGHT_BLUE)
        assertEquals(DarkThemeVariant.MIDNIGHT_BLUE, viewModel.uiState.value.darkThemeVariant)
        assertEquals(MidnightBackground, colorSchemeForVariant(DarkThemeVariant.MIDNIGHT_BLUE).background)

        viewModel.selectDarkThemeVariant(DarkThemeVariant.DEEP_CHARCOAL)
        assertEquals(DarkThemeVariant.DEEP_CHARCOAL, viewModel.uiState.value.darkThemeVariant)
        assertEquals(CharcoalBackground, colorSchemeForVariant(DarkThemeVariant.DEEP_CHARCOAL).background)
    }

    @Test
    fun `template engine generates results for all five tools`() {
        val titles = CreatorTemplateEngine.buildGamingTitles("Valorant Ranked")
        assertTrue(titles.isNotEmpty())

        val hashtags = CreatorTemplateEngine.buildHashtags("Minecraft Hardcore", lowercaseOnly = false)
        assertTrue(hashtags.primaryTags.isNotEmpty())
        assertTrue(hashtags.allFormattedBlock.contains("#MinecraftHardcore"))

        val script = CreatorTemplateEngine.buildVoiceoverScript(
            rawDetails = "Surviving 100 days",
            length = ScriptLength.MEDIUM,
            includeCues = true
        )
        assertNotNull(script)
        assertTrue(script!!.fullScript.contains("Surviving 100 days"))

        val prompts = CreatorTemplateEngine.buildThumbnailPrompts(
            rawIdea = "Neon samurai battle",
            aspectRatio = ThumbnailAspectRatio.VERTICAL_9_16
        )
        assertEquals(3, prompts.size)
        assertTrue(prompts.first().promptText.contains("9:16"))

        val ideas = CreatorTemplateEngine.buildVideoIdeas("Elden Ring")
        assertTrue(ideas.isNotEmpty())
    }
}
