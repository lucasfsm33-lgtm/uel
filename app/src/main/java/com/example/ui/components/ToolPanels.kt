package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CaptionStyle
import com.example.model.VideoClip
import com.example.model.VideoEffect
import com.example.model.VideoFilter
import com.example.model.VideoProject
import com.example.ui.theme.BlueAccentCyan
import com.example.ui.theme.BluePrimaryDark
import com.example.ui.theme.BlueSurface
import com.example.ui.theme.BlueSurfaceLight
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.TextOnGold
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.ActiveTool
import com.example.viewmodel.EditorViewModel

@Composable
fun ActiveToolPanel(
    activeTool: ActiveTool,
    viewModel: EditorViewModel,
    project: VideoProject,
    selectedClipIndex: Int,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (activeTool == ActiveTool.NONE) return

    val currentClip = project.clips.getOrNull(selectedClipIndex)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(BlueSurface)
            .border(width = 1.dp, color = Color(0x3300E5FF), shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .padding(14.dp)
            .testTag("active_tool_panel")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header with Tool title and close X
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when (activeTool) {
                        ActiveTool.EDIT -> "✂️ Edit Clip"
                        ActiveTool.AUDIO -> "🎵 Audio & Music"
                        ActiveTool.TEXT -> "✏️ Text & Titles"
                        ActiveTool.STICKERS -> "✨ Stickers & Emojis"
                        ActiveTool.EFFECTS -> "🌟 Video Effects"
                        ActiveTool.FILTERS -> "🎨 Color Filters"
                        ActiveTool.CAPTIONS -> "💬 AI Captions"
                        ActiveTool.NONE -> ""
                    },
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                IconButton(
                    onClick = onClose,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Tool",
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            when (activeTool) {
                ActiveTool.EDIT -> EditToolContent(
                    clip = currentClip,
                    onSplit = { viewModel.splitCurrentClip() },
                    onDelete = { viewModel.deleteCurrentClip() },
                    onDuplicate = { viewModel.duplicateCurrentClip() },
                    onSpeedChange = { viewModel.setClipSpeed(it) },
                    onVolumeChange = { viewModel.setClipVolume(it) }
                )
                ActiveTool.AUDIO -> AudioToolContent(
                    currentAudioName = project.audioTrack?.name ?: "None",
                    isMuted = project.audioTrack?.isMuted ?: false,
                    volume = project.audioTrack?.volume ?: 0.8f,
                    onSelectMusic = { viewModel.selectMusic(it) },
                    onToggleMute = { viewModel.toggleAudioMute() },
                    onVolumeChange = { viewModel.setAudioVolume(it) }
                )
                ActiveTool.TEXT -> TextToolContent(
                    currentText = project.textOverlays.firstOrNull()?.text ?: "Summer reel",
                    onUpdateText = { viewModel.updateMainText(it) },
                    onAddText = { viewModel.addTextOverlay(it) }
                )
                ActiveTool.STICKERS -> StickersToolContent(
                    onAddSticker = { viewModel.addSticker(it) }
                )
                ActiveTool.EFFECTS -> EffectsToolContent(
                    selectedEffect = currentClip?.effect ?: VideoEffect.NONE,
                    onSelectEffect = { viewModel.applyEffectToSelectedClip(it) }
                )
                ActiveTool.FILTERS -> FiltersToolContent(
                    selectedFilter = currentClip?.filter ?: VideoFilter.NORMAL,
                    onSelectFilter = { viewModel.applyFilterToSelectedClip(it) },
                    onApplyAll = { viewModel.applyFilterToAllClips(it) }
                )
                ActiveTool.CAPTIONS -> CaptionsToolContent(
                    captions = project.captions,
                    onGenerate = { viewModel.generateAutoCaptions() }
                )
                ActiveTool.NONE -> {}
            }
        }
    }
}

@Composable
private fun EditToolContent(
    clip: VideoClip?,
    onSplit: () -> Unit,
    onDelete: () -> Unit,
    onDuplicate: () -> Unit,
    onSpeedChange: (Float) -> Unit,
    onVolumeChange: (Float) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Split Action
            Button(
                onClick = onSplit,
                colors = ButtonDefaults.buttonColors(containerColor = BlueSurfaceLight),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("btn_action_split")
            ) {
                Icon(Icons.Default.ContentCut, contentDescription = null, tint = BlueAccentCyan, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Split", color = TextPrimary, fontSize = 12.sp)
            }

            // Duplicate Action
            Button(
                onClick = onDuplicate,
                colors = ButtonDefaults.buttonColors(containerColor = BlueSurfaceLight),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("btn_action_duplicate")
            ) {
                Icon(Icons.Default.ContentCopy, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Duplicate", color = TextPrimary, fontSize = 12.sp)
            }

            // Delete Action
            Button(
                onClick = onDelete,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B1E1E)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("btn_action_delete")
            ) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Delete", color = Color.White, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Speed presets (0.5x, 1x, 2x, 4x)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Speed, contentDescription = null, tint = BlueAccentCyan, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Speed:", color = TextSecondary, fontSize = 12.sp)
            Spacer(modifier = Modifier.width(10.dp))
            val currentSpeed = clip?.speed ?: 1.0f
            listOf(0.5f, 1.0f, 2.0f, 4.0f).forEach { spd ->
                val isSelected = currentSpeed == spd
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSelected) BlueAccentCyan else BlueSurfaceLight)
                        .clickable { onSpeedChange(spd) }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${spd}x",
                        color = if (isSelected) Color.Black else TextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun AudioToolContent(
    currentAudioName: String,
    isMuted: Boolean,
    volume: Float,
    onSelectMusic: (String) -> Unit,
    onToggleMute: () -> Unit,
    onVolumeChange: (Float) -> Unit
) {
    val tracks = listOf("Beach day", "Tropical Chill", "Summer Vlog", "Ocean Waves", "Lo-Fi Sunset")

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tracks.forEach { name ->
                val isSelected = name == currentAudioName
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) BlueAccentCyan else BlueSurfaceLight)
                        .clickable { onSelectMusic(name) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = name,
                        color = if (isSelected) Color.Black else TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onToggleMute, modifier = Modifier.size(32.dp)) {
                Icon(
                    imageVector = if (isMuted) Icons.Default.VolumeMute else Icons.Default.VolumeUp,
                    contentDescription = "Mute Toggle",
                    tint = if (isMuted) Color(0xFFFF5252) else BlueAccentCyan
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Slider(
                value = if (isMuted) 0f else volume,
                onValueChange = onVolumeChange,
                valueRange = 0f..1.5f,
                colors = SliderDefaults.colors(
                    thumbColor = BlueAccentCyan,
                    activeTrackColor = BlueAccentCyan,
                    inactiveTrackColor = BlueSurfaceLight
                ),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${((if (isMuted) 0f else volume) * 100).toInt()}%",
                color = TextSecondary,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun TextToolContent(
    currentText: String,
    onUpdateText: (String) -> Unit,
    onAddText: (String) -> Unit
) {
    var textInput by remember(currentText) { mutableStateOf(currentText) }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = textInput,
            onValueChange = {
                textInput = it
                onUpdateText(it)
            },
            label = { Text("Overlay Text", color = TextSecondary) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = GoldYellow,
                unfocusedBorderColor = BlueSurfaceLight
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_overlay_text")
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Summer reel", "Paradise Found 🌴", "Island Vibes", "Golden Hour ✨").forEach { preset ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(BlueSurfaceLight)
                        .clickable {
                            textInput = preset
                            onUpdateText(preset)
                        }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = preset, color = GoldYellow, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun StickersToolContent(
    onAddSticker: (String) -> Unit
) {
    val stickers = listOf("🌴", "☀️", "🌊", "✈️", "🕶️", "🔥", "🏖️", "🌺", "🍹", "🍉", "🏄‍♂️", "✨", "💙")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        stickers.forEach { emoji ->
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(BlueSurfaceLight)
                    .clickable { onAddSticker(emoji) },
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, fontSize = 24.sp)
            }
        }
    }
}

@Composable
private fun EffectsToolContent(
    selectedEffect: VideoEffect,
    onSelectEffect: (VideoEffect) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        VideoEffect.values().forEach { effect ->
            val isSelected = effect == selectedEffect
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) Color(0x3300E5FF) else BlueSurfaceLight)
                    .clickable { onSelectEffect(effect) }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = effect.icon, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = effect.label,
                    color = if (isSelected) BlueAccentCyan else TextPrimary,
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
private fun FiltersToolContent(
    selectedFilter: VideoFilter,
    onSelectFilter: (VideoFilter) -> Unit,
    onApplyAll: (VideoFilter) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            VideoFilter.values().forEach { filter ->
                val isSelected = filter == selectedFilter
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) BlueAccentCyan else BlueSurfaceLight)
                        .clickable { onSelectFilter(filter) }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = filter.label,
                        color = if (isSelected) Color.Black else TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { onApplyAll(selectedFilter) },
            colors = ButtonDefaults.buttonColors(containerColor = BlueSurfaceLight),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Apply to all clips", color = BlueAccentCyan, fontSize = 11.sp)
        }
    }
}

@Composable
private fun CaptionsToolContent(
    captions: List<com.example.model.CaptionItem>,
    onGenerate: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = onGenerate,
            colors = ButtonDefaults.buttonColors(containerColor = GoldYellow, contentColor = TextOnGold),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("btn_generate_auto_captions")
        ) {
            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = TextOnGold, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("BlueGPT Auto-Captions Generator", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }

        if (captions.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                captions.forEach { cap ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(BlueSurfaceLight)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(text = cap.text, color = Color.White, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}
