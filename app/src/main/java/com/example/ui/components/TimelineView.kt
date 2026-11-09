package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SampleProjects
import com.example.model.VideoClip
import com.example.model.VideoProject
import com.example.ui.theme.AudioBadgeBlue
import com.example.ui.theme.AudioTrackBlue
import com.example.ui.theme.AudioWaveColor
import com.example.ui.theme.BlueAccentCyan
import com.example.ui.theme.BlueTimelineBg
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.PlayheadWhite
import com.example.ui.theme.TextOnGold
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextTertiary

// Standard width scale: 1 second = 36 dp
private const val DP_PER_SECOND = 36f

@Composable
fun TimelineView(
    project: VideoProject,
    currentTimeMs: Long,
    selectedClipIndex: Int,
    onClipSelected: (Int) -> Unit,
    onSeek: (Long) -> Unit,
    onAddAudioClick: () -> Unit,
    onEditTextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalMs = project.totalDurationMs.coerceAtLeast(1000L)
    val totalSeconds = (totalMs / 1000f)
    val timelineWidthDp = (totalSeconds * DP_PER_SECOND + 120f).dp

    val scrollState = rememberScrollState()

    // Calculate playhead x offset
    val playheadFraction = (currentTimeMs.toFloat() / totalMs).coerceIn(0f, 1f)
    val playheadOffsetDp = ((currentTimeMs / 1000f) * DP_PER_SECOND + 24f).dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(BlueTimelineBg)
            .testTag("timeline_view")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .width(timelineWidthDp)
                .padding(vertical = 4.dp)
                .pointerInput(totalMs) {
                    detectTapGestures { offset ->
                        val clickedSeconds = ((offset.x - 24.dp.toPx()) / (DP_PER_SECOND * density)).coerceAtLeast(0f)
                        val targetMs = (clickedSeconds * 1000).toLong().coerceIn(0L, totalMs)
                        onSeek(targetMs)
                    }
                }
                .pointerInput(totalMs) {
                    detectDragGestures { change, _ ->
                        change.consume()
                        val currentX = change.position.x
                        val clickedSeconds = ((currentX - 24.dp.toPx()) / (DP_PER_SECOND * density)).coerceAtLeast(0f)
                        val targetMs = (clickedSeconds * 1000).toLong().coerceIn(0L, totalMs)
                        onSeek(targetMs)
                    }
                }
        ) {
            // 1. Time Ruler (00:00, 00:05, 00:10, 00:15, 00:20, 00:24)
            TimeRuler(
                totalSeconds = totalSeconds.toInt(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .padding(start = 24.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // 2. Video Clips Track (Mockup: Thumbnail frames side-by-side)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp)
                    .padding(start = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                project.clips.forEachIndexed { index, clip ->
                    VideoClipItem(
                        clip = clip,
                        isSelected = index == selectedClipIndex,
                        onClick = { onClipSelected(index) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // 3. Text / Subtitle Track (Mockup: Yellow banner "T Summer reel")
            val primaryText = project.textOverlays.firstOrNull()?.text ?: "Summer reel"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .padding(start = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Yellow text bar segment
                val textDurationSec = project.textOverlays.firstOrNull()?.let { it.durationMs / 1000f } ?: 18f
                val textBarWidth = (textDurationSec * DP_PER_SECOND).dp

                Row(
                    modifier = Modifier
                        .width(textBarWidth)
                        .height(30.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(GoldYellow)
                        .clickable(onClick = onEditTextClick)
                        .padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Title,
                        contentDescription = "Text Track",
                        tint = TextOnGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = primaryText,
                        color = TextOnGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // 4. Audio Track (Mockup: "+ Add audio" and Waveform strip with "Beach day" badge)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp)
            ) {
                // "+ Add audio" pill button
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF1E88E5))
                        .clickable(onClick = onAddAudioClick)
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.GraphicEq,
                        contentDescription = "Add Audio",
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Add audio",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Audio Waveform Track
                val audioTrack = project.audioTrack
                val trackWidth = (totalSeconds * DP_PER_SECOND).dp
                Box(
                    modifier = Modifier
                        .width(trackWidth)
                        .height(34.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(AudioTrackBlue)
                        .padding(horizontal = 6.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    // Audio waveform visualization Canvas
                    val points = audioTrack?.waveformPoints?.ifEmpty { SampleProjects.defaultWaveform }
                        ?: SampleProjects.defaultWaveform

                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(26.dp)
                    ) {
                        val barCount = points.size
                        if (barCount > 0) {
                            val spacing = size.width / barCount
                            val centerY = size.height / 2f
                            points.forEachIndexed { i, amp ->
                                val x = i * spacing + spacing / 2f
                                val barHeight = (amp * (size.height * 0.85f)).coerceAtLeast(3f)
                                drawLine(
                                    color = AudioWaveColor,
                                    start = Offset(x, centerY - barHeight / 2),
                                    end = Offset(x, centerY + barHeight / 2),
                                    strokeWidth = 2.dp.toPx()
                                )
                            }
                        }
                    }

                    // Track title badge on right ("Beach day")
                    if (audioTrack != null) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 10.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(AudioBadgeBlue)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = audioTrack.name,
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // Draggable Playhead Scrubber (White vertical line + top circular handle)
        // Positioned absolutely within the timeline scroll container
        Box(
            modifier = Modifier
                .offset { IntOffset(x = (playheadOffsetDp.toPx() - scrollState.value).toInt(), y = 0) }
                .width(2.dp)
                .fillMaxHeight()
                .background(PlayheadWhite)
                .testTag("playhead_indicator")
        ) {
            // Circle on top of playhead needle
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .offset(x = (-6).dp, y = 14.dp)
                    .clip(CircleShape)
                    .background(PlayheadWhite)
            )
        }
    }
}

@Composable
private fun VideoClipItem(
    clip: VideoClip,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val clipSeconds = (clip.durationMs / clip.speed) / 1000f
    val clipWidthDp = (clipSeconds * DP_PER_SECOND).coerceAtLeast(50f).dp

    Box(
        modifier = Modifier
            .width(clipWidthDp)
            .height(68.dp)
            .clip(RoundedCornerShape(6.dp))
            .border(
                width = if (isSelected) 2.dp else 0.5.dp,
                color = if (isSelected) Color.White else Color(0x33FFFFFF),
                shape = RoundedCornerShape(6.dp)
            )
            .clickable(onClick = onClick)
    ) {
        if (clip.thumbnailRes != 0) {
            Image(
                painter = painterResource(id = clip.thumbnailRes),
                contentDescription = clip.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF1E3A6E))
            )
        }

        // Clip Speed / Filter tag if modified
        if (clip.speed != 1.0f) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(3.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0xCC000000))
                    .padding(horizontal = 4.dp, vertical = 1.dp)
            ) {
                Text(
                    text = "${clip.speed}x",
                    color = BlueAccentCyan,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun TimeRuler(
    totalSeconds: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val step = 5
        val maxSteps = (totalSeconds / step) + 1
        for (i in 0..maxSteps) {
            val sec = i * step
            val min = sec / 60
            val s = sec % 60
            val label = String.format("%02d:%02d", min, s)

            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.width((step * DP_PER_SECOND).dp)
            ) {
                Text(
                    text = label,
                    color = TextTertiary,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Normal
                )
                Canvas(modifier = Modifier.size(width = 1.dp, height = 6.dp)) {
                    drawLine(
                        color = Color(0x66FFFFFF),
                        start = Offset(0f, 0f),
                        end = Offset(0f, size.height),
                        strokeWidth = 1.dp.toPx()
                    )
                }
            }
        }
    }
}
