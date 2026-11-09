package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.VideoClip
import com.example.model.VideoEffect
import com.example.model.VideoFilter
import com.example.model.VideoProject

@Composable
fun VideoPreviewPlayer(
    project: VideoProject,
    currentTimeMs: Long,
    isPlaying: Boolean,
    onTogglePlayPause: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalMs = project.totalDurationMs.coerceAtLeast(1L)
    val formattedCurrent = formatTimestamp(currentTimeMs)
    val formattedTotal = formatTimestamp(totalMs)

    // Determine current active clip based on currentTimeMs
    var accumulated = 0L
    var activeClip: VideoClip = project.clips.firstOrNull() ?: VideoClip("empty", "", R.drawable.video_preview_beach, 5000L)
    for (clip in project.clips) {
        val clipEffective = (clip.durationMs / clip.speed).toLong()
        if (currentTimeMs in accumulated..(accumulated + clipEffective)) {
            activeClip = clip
            break
        }
        accumulated += clipEffective
    }

    // Active text overlay at currentTimeMs
    val activeText = project.textOverlays.firstOrNull {
        currentTimeMs in it.startTimeMs..(it.startTimeMs + it.durationMs)
    }

    // Active sticker at currentTimeMs
    val activeSticker = project.stickers.firstOrNull {
        currentTimeMs in it.startTimeMs..(it.startTimeMs + it.durationMs)
    }

    // Active caption at currentTimeMs
    val activeCaption = project.captions.firstOrNull {
        currentTimeMs in it.startTimeMs..it.endTimeMs
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .aspectRatio(1.08f) // Rounded preview container matching mockup ratio
            .clip(RoundedCornerShape(18.dp))
            .background(Color.Black)
            .clickable(onClick = onTogglePlayPause)
            .testTag("video_preview_player"),
        contentAlignment = Alignment.Center
    ) {
        // Video Frame (Beach scene)
        val imageRes = if (project.clips.size > 1 && activeClip.thumbnailRes != 0) {
            // If the first clip is active, use the high-res beach preview
            if (activeClip.id == "clip_1") R.drawable.video_preview_beach else activeClip.thumbnailRes
        } else {
            R.drawable.video_preview_beach
        }

        val colorFilter = getColorFilterFor(activeClip.filter)

        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Video Frame",
            contentScale = ContentScale.Crop,
            colorFilter = colorFilter,
            modifier = Modifier.fillMaxSize()
        )

        // Visual Effect Overlay
        when (activeClip.effect) {
            VideoEffect.CINEMATIC_GLOW -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x33FFECB3))
                )
            }
            VideoEffect.LENS_FLARE -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x26FFE082))
                )
            }
            VideoEffect.VHS_RETRO -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x1F00E5FF))
                )
            }
            VideoEffect.DREAMY_BLUR -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x22FFFFFF))
                )
            }
            else -> {}
        }

        // Active Text Overlay on Video (e.g. "Summer reel" in yellow banner or stylized text)
        if (activeText != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 28.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(activeText.colorHex))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = activeText.text,
                    color = Color.Black,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Active Caption Subtitle at bottom center
        if (activeCaption != null && activeText == null) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 44.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xB3000000))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = activeCaption.text,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Active Sticker
        if (activeSticker != null) {
            Text(
                text = activeSticker.symbol,
                fontSize = 32.sp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 20.dp, end = 20.dp)
            )
        }

        // Center Frosted Play Button (Mockup: circular frosted white with dark play arrow)
        AnimatedVisibility(
            visible = !isPlaying,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color(0xCCFFFFFF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play Video",
                    tint = Color(0xFF0F1E36),
                    modifier = Modifier.size(34.dp)
                )
            }
        }

        // Bottom-Left Timecode Badge (Mockup: "00:07 / 00:24")
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 14.dp, bottom = 14.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xB3000000))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = "$formattedCurrent / $formattedTotal",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

fun formatTimestamp(ms: Long): String {
    val totalSec = ms / 1000
    val minutes = totalSec / 60
    val seconds = totalSec % 60
    return String.format("%02d:%02d", minutes, seconds)
}

private fun getColorFilterFor(filter: VideoFilter): ColorFilter? {
    return when (filter) {
        VideoFilter.NORMAL -> null
        VideoFilter.VIBRANT_BLUE -> {
            val matrix = ColorMatrix(
                floatArrayOf(
                    0.9f, 0f, 0f, 0f, 0f,
                    0f, 1.0f, 0f, 0f, 0f,
                    0f, 0f, 1.35f, 0f, 15f,
                    0f, 0f, 0f, 1.0f, 0f
                )
            )
            ColorFilter.colorMatrix(matrix)
        }
        VideoFilter.SUNSET_WARM -> {
            val matrix = ColorMatrix(
                floatArrayOf(
                    1.25f, 0f, 0f, 0f, 15f,
                    0f, 1.05f, 0f, 0f, 5f,
                    0f, 0f, 0.85f, 0f, -10f,
                    0f, 0f, 0f, 1.0f, 0f
                )
            )
            ColorFilter.colorMatrix(matrix)
        }
        VideoFilter.AQUA_BEACH -> {
            val matrix = ColorMatrix(
                floatArrayOf(
                    0.85f, 0f, 0f, 0f, -5f,
                    0f, 1.15f, 0f, 0f, 10f,
                    0f, 0f, 1.25f, 0f, 20f,
                    0f, 0f, 0f, 1.0f, 0f
                )
            )
            ColorFilter.colorMatrix(matrix)
        }
        VideoFilter.GOLDEN_HOUR -> {
            val matrix = ColorMatrix(
                floatArrayOf(
                    1.2f, 0f, 0f, 0f, 20f,
                    0f, 1.15f, 0f, 0f, 15f,
                    0f, 0f, 0.9f, 0f, 0f,
                    0f, 0f, 0f, 1.0f, 0f
                )
            )
            ColorFilter.colorMatrix(matrix)
        }
        VideoFilter.FILM_NOIR -> {
            val matrix = ColorMatrix().apply { setToSaturation(0f) }
            ColorFilter.colorMatrix(matrix)
        }
        VideoFilter.CYBERPUNK -> {
            val matrix = ColorMatrix(
                floatArrayOf(
                    1.2f, 0f, 0.2f, 0f, 10f,
                    0f, 0.8f, 0.2f, 0f, -10f,
                    0.2f, 0f, 1.4f, 0f, 25f,
                    0f, 0f, 0f, 1.0f, 0f
                )
            )
            ColorFilter.colorMatrix(matrix)
        }
    }
}
