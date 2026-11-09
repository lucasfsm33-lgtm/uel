package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SampleProjects
import com.example.model.AiScriptIdea
import com.example.model.AudioTrack
import com.example.model.BlueGptAssistant
import com.example.model.BlueGptMessage
import com.example.model.CaptionItem
import com.example.model.StickerItem
import com.example.model.TextOverlay
import com.example.model.VideoClip
import com.example.model.VideoEffect
import com.example.model.VideoFilter
import com.example.model.VideoProject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

enum class ActiveTool {
    NONE,
    EDIT,
    AUDIO,
    TEXT,
    STICKERS,
    EFFECTS,
    FILTERS,
    CAPTIONS
}

data class EditorUiState(
    val currentProject: VideoProject = SampleProjects.summerReelProject,
    val isPlaying: Boolean = false,
    val currentTimeMs: Long = 7000L, // Matches 00:07 / 00:24 from the mockup
    val selectedClipIndex: Int = 0,
    val selectedResolution: String = "1080p",
    val activeTool: ActiveTool = ActiveTool.NONE,
    val showBlueGptHub: Boolean = false,
    val showExportDialog: Boolean = false,
    val isExporting: Boolean = false,
    val exportProgress: Float = 0f,
    val exportComplete: Boolean = false,
    val blueGptMessages: List<BlueGptMessage> = listOf(
        BlueGptMessage(
            id = "m1",
            sender = "BlueGPT",
            content = "Hey! I'm BlueGPT, your AI video assistant. I analyzed your 'Summer reel' project (00:24). How can I enhance your video today?",
            timestamp = "Just now"
        )
    ),
    val aiScriptGenerated: AiScriptIdea? = null,
    val canUndo: Boolean = false,
    val canRedo: Boolean = false
)

class EditorViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(EditorUiState())
    val uiState: StateFlow<EditorUiState> = _uiState.asStateFlow()

    private var playbackJob: Job? = null
    private val undoStack = mutableListOf<VideoProject>()
    private val redoStack = mutableListOf<VideoProject>()

    init {
        // Initialize with default state
        updateUndoRedoAvailability()
    }

    private fun saveUndoSnapshot() {
        undoStack.add(_uiState.value.currentProject)
        redoStack.clear()
        if (undoStack.size > 20) undoStack.removeAt(0)
        updateUndoRedoAvailability()
    }

    private fun updateUndoRedoAvailability() {
        _uiState.update {
            it.copy(
                canUndo = undoStack.isNotEmpty(),
                canRedo = redoStack.isNotEmpty()
            )
        }
    }

    fun undo() {
        if (undoStack.isNotEmpty()) {
            val previous = undoStack.removeAt(undoStack.lastIndex)
            redoStack.add(_uiState.value.currentProject)
            _uiState.update { it.copy(currentProject = previous) }
            updateUndoRedoAvailability()
        }
    }

    fun redo() {
        if (redoStack.isNotEmpty()) {
            val next = redoStack.removeAt(redoStack.lastIndex)
            undoStack.add(_uiState.value.currentProject)
            _uiState.update { it.copy(currentProject = next) }
            updateUndoRedoAvailability()
        }
    }

    fun togglePlayPause() {
        val willPlay = !_uiState.value.isPlaying
        _uiState.update { it.copy(isPlaying = willPlay) }
        if (willPlay) {
            startPlaybackLoop()
        } else {
            playbackJob?.cancel()
        }
    }

    private fun startPlaybackLoop() {
        playbackJob?.cancel()
        playbackJob = viewModelScope.launch {
            while (isActive && _uiState.value.isPlaying) {
                delay(50)
                val total = _uiState.value.currentProject.totalDurationMs
                if (total <= 0) break
                val next = _uiState.value.currentTimeMs + 50
                if (next >= total) {
                    _uiState.update { it.copy(currentTimeMs = 0L) }
                } else {
                    _uiState.update { it.copy(currentTimeMs = next) }
                }
            }
        }
    }

    fun seekTo(timeMs: Long) {
        val total = _uiState.value.currentProject.totalDurationMs
        val clamped = timeMs.coerceIn(0L, total)
        _uiState.update { it.copy(currentTimeMs = clamped) }
    }

    fun selectClip(index: Int) {
        val clips = _uiState.value.currentProject.clips
        if (index in clips.indices) {
            _uiState.update { it.copy(selectedClipIndex = index) }
        }
    }

    fun setActiveTool(tool: ActiveTool) {
        _uiState.update {
            it.copy(activeTool = if (it.activeTool == tool) ActiveTool.NONE else tool)
        }
    }

    fun closeActiveTool() {
        _uiState.update { it.copy(activeTool = ActiveTool.NONE) }
    }

    fun toggleBlueGptHub(show: Boolean) {
        _uiState.update { it.copy(showBlueGptHub = show) }
    }

    fun setResolution(resolution: String) {
        _uiState.update { it.copy(selectedResolution = resolution) }
    }

    // Clip Editing Operations
    fun splitCurrentClip() {
        saveUndoSnapshot()
        val currentClips = _uiState.value.currentProject.clips.toMutableList()
        val index = _uiState.value.selectedClipIndex
        if (index in currentClips.indices) {
            val clip = currentClips[index]
            val halfDuration = clip.durationMs / 2
            if (halfDuration > 500) {
                val clipA = clip.copy(id = "${clip.id}_a", durationMs = halfDuration)
                val clipB = clip.copy(id = "${clip.id}_b", durationMs = clip.durationMs - halfDuration)
                currentClips[index] = clipA
                currentClips.add(index + 1, clipB)
                _uiState.update {
                    it.copy(currentProject = it.currentProject.copy(clips = currentClips))
                }
            }
        }
    }

    fun deleteCurrentClip() {
        val currentClips = _uiState.value.currentProject.clips
        if (currentClips.size > 1) {
            saveUndoSnapshot()
            val index = _uiState.value.selectedClipIndex
            val newClips = currentClips.toMutableList().apply { removeAt(index) }
            val newIndex = (index - 1).coerceAtLeast(0)
            _uiState.update {
                it.copy(
                    currentProject = it.currentProject.copy(clips = newClips),
                    selectedClipIndex = newIndex
                )
            }
        }
    }

    fun duplicateCurrentClip() {
        saveUndoSnapshot()
        val currentClips = _uiState.value.currentProject.clips.toMutableList()
        val index = _uiState.value.selectedClipIndex
        if (index in currentClips.indices) {
            val clip = currentClips[index]
            val duplicate = clip.copy(id = "${clip.id}_copy_${System.currentTimeMillis()}")
            currentClips.add(index + 1, duplicate)
            _uiState.update {
                it.copy(
                    currentProject = it.currentProject.copy(clips = currentClips),
                    selectedClipIndex = index + 1
                )
            }
        }
    }

    fun setClipSpeed(speed: Float) {
        saveUndoSnapshot()
        val currentClips = _uiState.value.currentProject.clips.toMutableList()
        val index = _uiState.value.selectedClipIndex
        if (index in currentClips.indices) {
            currentClips[index] = currentClips[index].copy(speed = speed)
            _uiState.update {
                it.copy(currentProject = it.currentProject.copy(clips = currentClips))
            }
        }
    }

    fun setClipVolume(volume: Float) {
        saveUndoSnapshot()
        val currentClips = _uiState.value.currentProject.clips.toMutableList()
        val index = _uiState.value.selectedClipIndex
        if (index in currentClips.indices) {
            currentClips[index] = currentClips[index].copy(volume = volume)
            _uiState.update {
                it.copy(currentProject = it.currentProject.copy(clips = currentClips))
            }
        }
    }

    fun applyFilterToSelectedClip(filter: VideoFilter) {
        saveUndoSnapshot()
        val currentClips = _uiState.value.currentProject.clips.toMutableList()
        val index = _uiState.value.selectedClipIndex
        if (index in currentClips.indices) {
            currentClips[index] = currentClips[index].copy(filter = filter)
            _uiState.update {
                it.copy(currentProject = it.currentProject.copy(clips = currentClips))
            }
        }
    }

    fun applyFilterToAllClips(filter: VideoFilter) {
        saveUndoSnapshot()
        val updated = _uiState.value.currentProject.clips.map { it.copy(filter = filter) }
        _uiState.update {
            it.copy(currentProject = it.currentProject.copy(clips = updated))
        }
    }

    fun applyEffectToSelectedClip(effect: VideoEffect) {
        saveUndoSnapshot()
        val currentClips = _uiState.value.currentProject.clips.toMutableList()
        val index = _uiState.value.selectedClipIndex
        if (index in currentClips.indices) {
            currentClips[index] = currentClips[index].copy(effect = effect)
            _uiState.update {
                it.copy(currentProject = it.currentProject.copy(clips = currentClips))
            }
        }
    }

    // Text & Subtitles Operations
    fun updateMainText(text: String) {
        saveUndoSnapshot()
        val currentTextList = _uiState.value.currentProject.textOverlays.toMutableList()
        if (currentTextList.isNotEmpty()) {
            currentTextList[0] = currentTextList[0].copy(text = text)
        } else {
            currentTextList.add(
                TextOverlay(
                    id = "text_${System.currentTimeMillis()}",
                    text = text,
                    startTimeMs = 0L,
                    durationMs = 15000L
                )
            )
        }
        _uiState.update {
            it.copy(currentProject = it.currentProject.copy(textOverlays = currentTextList))
        }
    }

    fun addTextOverlay(text: String, colorHex: Long = 0xFFFFD54F) {
        saveUndoSnapshot()
        val start = _uiState.value.currentTimeMs
        val newOverlay = TextOverlay(
            id = "text_${System.currentTimeMillis()}",
            text = text,
            startTimeMs = start,
            durationMs = 5000L,
            colorHex = colorHex
        )
        val list = _uiState.value.currentProject.textOverlays + newOverlay
        _uiState.update {
            it.copy(currentProject = it.currentProject.copy(textOverlays = list))
        }
    }

    // Audio Operations
    fun selectMusic(trackName: String) {
        saveUndoSnapshot()
        val newAudio = AudioTrack(
            id = "audio_${System.currentTimeMillis()}",
            name = trackName,
            durationMs = _uiState.value.currentProject.totalDurationMs,
            waveformPoints = SampleProjects.defaultWaveform
        )
        _uiState.update {
            it.copy(currentProject = it.currentProject.copy(audioTrack = newAudio))
        }
    }

    fun toggleAudioMute() {
        val currentAudio = _uiState.value.currentProject.audioTrack ?: return
        saveUndoSnapshot()
        val updated = currentAudio.copy(isMuted = !currentAudio.isMuted)
        _uiState.update {
            it.copy(currentProject = it.currentProject.copy(audioTrack = updated))
        }
    }

    fun setAudioVolume(volume: Float) {
        val currentAudio = _uiState.value.currentProject.audioTrack ?: return
        saveUndoSnapshot()
        val updated = currentAudio.copy(volume = volume)
        _uiState.update {
            it.copy(currentProject = it.currentProject.copy(audioTrack = updated))
        }
    }

    // Stickers
    fun addSticker(symbol: String) {
        saveUndoSnapshot()
        val start = _uiState.value.currentTimeMs
        val newSticker = StickerItem(
            id = "sticker_${System.currentTimeMillis()}",
            symbol = symbol,
            startTimeMs = start,
            durationMs = 6000L
        )
        val list = _uiState.value.currentProject.stickers + newSticker
        _uiState.update {
            it.copy(currentProject = it.currentProject.copy(stickers = list))
        }
    }

    // Captions & BlueGPT AI
    fun generateAutoCaptions() {
        saveUndoSnapshot()
        val autoCaptions = listOf(
            CaptionItem("cap_1", "Escape to paradise 🌊", 0L, 4000L),
            CaptionItem("cap_2", "Sun, sand & endless summer", 4000L, 10000L),
            CaptionItem("cap_3", "Golden hour magic ✨", 10000L, 18000L),
            CaptionItem("cap_4", "Making memories by the sea 🏖️", 18000L, 24000L)
        )
        _uiState.update {
            it.copy(currentProject = it.currentProject.copy(captions = autoCaptions))
        }
    }

    fun generateAiScript(prompt: String) {
        val idea = BlueGptAssistant.generateScriptFromPrompt(prompt)
        val aiMessage = BlueGptMessage(
            id = "msg_${System.currentTimeMillis()}",
            sender = "BlueGPT",
            content = "🎬 I generated a new script for you: **${idea.title}** (${idea.genre})!\n\n🪝 **Hook**: ${idea.hook}\n\n🎵 **Suggested Audio**: ${idea.musicSuggestion}\n⏱️ **Duration**: ${idea.estimatedDuration}",
            timestamp = "Just now"
        )
        _uiState.update {
            it.copy(
                aiScriptGenerated = idea,
                blueGptMessages = it.blueGptMessages + aiMessage
            )
        }
    }

    fun sendBlueGptQuery(query: String) {
        val userMsg = BlueGptMessage(
            id = "u_${System.currentTimeMillis()}",
            sender = "User",
            content = query,
            timestamp = "Just now"
        )
        val responseText = BlueGptAssistant.getBlueGptResponse(query)
        val aiMsg = BlueGptMessage(
            id = "ai_${System.currentTimeMillis() + 1}",
            sender = "BlueGPT",
            content = responseText,
            timestamp = "Just now"
        )
        _uiState.update {
            it.copy(blueGptMessages = it.blueGptMessages + userMsg + aiMsg)
        }
    }

    fun loadProject(project: VideoProject) {
        playbackJob?.cancel()
        undoStack.clear()
        redoStack.clear()
        _uiState.update {
            it.copy(
                currentProject = project,
                currentTimeMs = 0L,
                isPlaying = false,
                selectedClipIndex = 0,
                showBlueGptHub = false
            )
        }
        updateUndoRedoAvailability()
    }

    // Export Process
    fun showExportDialog(show: Boolean) {
        _uiState.update {
            it.copy(
                showExportDialog = show,
                isExporting = false,
                exportProgress = 0f,
                exportComplete = false
            )
        }
    }

    fun startExport() {
        _uiState.update { it.copy(isExporting = true, exportProgress = 0f, exportComplete = false) }
        viewModelScope.launch {
            for (step in 1..20) {
                delay(80)
                _uiState.update { it.copy(exportProgress = step * 0.05f) }
            }
            _uiState.update { it.copy(isExporting = false, exportComplete = true) }
        }
    }
}
