package com.example

import com.example.data.SampleProjects
import com.example.model.BlueGptAssistant
import com.example.model.VideoFilter
import com.example.viewmodel.ActiveTool
import com.example.viewmodel.EditorViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun projectDuration_isCalculatedCorrectly() {
        val project = SampleProjects.summerReelProject
        assertEquals(24000L, project.totalDurationMs)
        assertEquals(4, project.clips.size)
        assertEquals("Summer reel", project.title)
    }

    @Test
    fun blueGptAssistant_generatesScriptForPrompts() {
        val summerScript = BlueGptAssistant.generateScriptFromPrompt("summer beach vlog")
        assertTrue(summerScript.title.contains("Summer") || summerScript.title.contains("Paradise"))
        assertEquals("24 seconds", summerScript.estimatedDuration)

        val coffeeScript = BlueGptAssistant.generateScriptFromPrompt("morning coffee routine")
        assertTrue(coffeeScript.title.contains("Coffee") || coffeeScript.title.contains("Morning"))
        assertEquals("15 seconds", coffeeScript.estimatedDuration)

        val cyberScript = BlueGptAssistant.generateScriptFromPrompt("tokyo cyberpunk neon")
        assertTrue(cyberScript.title.contains("Tokyo") || cyberScript.title.contains("Neon"))
    }

    @Test
    fun blueGptAssistant_answersQueries() {
        val hookResponse = BlueGptAssistant.getBlueGptResponse("how to make a viral hook?")
        assertTrue(hookResponse.contains("Hook Formula"))

        val filterResponse = BlueGptAssistant.getBlueGptResponse("what color filter should I use?")
        assertTrue(filterResponse.contains("Vibrant Blue"))
    }

    @Test
    fun editorViewModel_stateOperations() {
        val viewModel = EditorViewModel()
        val initial = viewModel.uiState.value

        assertEquals(7000L, initial.currentTimeMs)
        assertFalse(initial.isPlaying)
        assertEquals("1080p", initial.selectedResolution)

        // Test Tool toggling
        viewModel.setActiveTool(ActiveTool.EDIT)
        assertEquals(ActiveTool.EDIT, viewModel.uiState.value.activeTool)

        viewModel.closeActiveTool()
        assertEquals(ActiveTool.NONE, viewModel.uiState.value.activeTool)

        // Test Seek
        viewModel.seekTo(12000L)
        assertEquals(12000L, viewModel.uiState.value.currentTimeMs)

        // Test Split
        val clipCountBefore = viewModel.uiState.value.currentProject.clips.size
        viewModel.splitCurrentClip()
        val clipCountAfter = viewModel.uiState.value.currentProject.clips.size
        assertEquals(clipCountBefore + 1, clipCountAfter)

        // Test Undo
        assertTrue(viewModel.uiState.value.canUndo)
        viewModel.undo()
        assertEquals(clipCountBefore, viewModel.uiState.value.currentProject.clips.size)

        // Test Filter application
        viewModel.applyFilterToSelectedClip(VideoFilter.SUNSET_WARM)
        assertEquals(VideoFilter.SUNSET_WARM, viewModel.uiState.value.currentProject.clips[0].filter)

        // Test Resolution change
        viewModel.setResolution("4K")
        assertEquals("4K", viewModel.uiState.value.selectedResolution)
    }
}
