package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ui.components.ActiveToolPanel
import com.example.ui.components.BlueGptHubScreen
import com.example.ui.components.CapCutToolbar
import com.example.ui.components.ExportDialog
import com.example.ui.components.PlaybackControlsBar
import com.example.ui.components.TimelineView
import com.example.ui.components.TopEditorBar
import com.example.ui.components.VideoPreviewPlayer
import com.example.ui.theme.BlueBackground
import com.example.viewmodel.ActiveTool
import com.example.viewmodel.EditorViewModel

@Composable
fun EditorScreen(
    viewModel: EditorViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.showBlueGptHub) {
        BlueGptHubScreen(
            viewModel = viewModel,
            onBackToEditor = { viewModel.toggleBlueGptHub(false) },
            modifier = modifier
                .statusBarsPadding()
                .navigationBarsPadding()
        )
        return
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BlueBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 1. Top Editor Bar (X close, 1080p pill, BlueGPT, Export button)
            TopEditorBar(
                selectedResolution = uiState.selectedResolution,
                onResolutionChange = { viewModel.setResolution(it) },
                onCloseClick = { viewModel.toggleBlueGptHub(true) },
                onExportClick = { viewModel.showExportDialog(true) },
                onBlueGptClick = { viewModel.toggleBlueGptHub(true) }
            )

            // 2. Video Preview Player (Rounded preview with beach scene, overlays & frosted play button)
            VideoPreviewPlayer(
                project = uiState.currentProject,
                currentTimeMs = uiState.currentTimeMs,
                isPlaying = uiState.isPlaying,
                onTogglePlayPause = { viewModel.togglePlayPause() }
            )

            // 3. Playback Controls Bar (Undo, Redo, White Play/Pause, 00:07 / 00:24)
            PlaybackControlsBar(
                isPlaying = uiState.isPlaying,
                currentTimeMs = uiState.currentTimeMs,
                totalDurationMs = uiState.currentProject.totalDurationMs,
                canUndo = uiState.canUndo,
                canRedo = uiState.canRedo,
                onUndoClick = { viewModel.undo() },
                onRedoClick = { viewModel.redo() },
                onPlayPauseClick = { viewModel.togglePlayPause() }
            )

            // 4. Multi-Track Timeline (Ruler, Scrubber needle, Clips, Text banner, Audio waveform)
            TimelineView(
                project = uiState.currentProject,
                currentTimeMs = uiState.currentTimeMs,
                selectedClipIndex = uiState.selectedClipIndex,
                onClipSelected = { viewModel.selectClip(it) },
                onSeek = { viewModel.seekTo(it) },
                onAddAudioClick = { viewModel.setActiveTool(ActiveTool.AUDIO) },
                onEditTextClick = { viewModel.setActiveTool(ActiveTool.TEXT) },
                modifier = Modifier.weight(1f)
            )

            // 5. Expandable Sub-Tool Panel when active
            AnimatedVisibility(
                visible = uiState.activeTool != ActiveTool.NONE,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                ActiveToolPanel(
                    activeTool = uiState.activeTool,
                    viewModel = viewModel,
                    project = uiState.currentProject,
                    selectedClipIndex = uiState.selectedClipIndex,
                    onClose = { viewModel.closeActiveTool() }
                )
            }

            // 6. Bottom CapCut Action Toolbar (Edit, Audio, Text, Stickers, Effects, Filters, Captions)
            CapCutToolbar(
                activeTool = uiState.activeTool,
                onToolClick = { viewModel.setActiveTool(it) }
            )
        }

        // Export Dialog Modal
        if (uiState.showExportDialog) {
            ExportDialog(
                selectedResolution = uiState.selectedResolution,
                isExporting = uiState.isExporting,
                exportProgress = uiState.exportProgress,
                exportComplete = uiState.exportComplete,
                onResolutionChange = { viewModel.setResolution(it) },
                onStartExport = { viewModel.startExport() },
                onDismiss = { viewModel.showExportDialog(false) }
            )
        }
    }
}
